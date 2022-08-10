function P6eImageCodecDecryptor() {
}

P6eImageCodecDecryptor.AES = function () {
};
P6eImageCodecDecryptor.MD5 = function () {
};
P6eImageCodecDecryptor.UUID = function () {
};
P6eImageCodecDecryptor.Compiler = function () {
};
P6eImageCodecDecryptor.NumberGenerator = function () {
};

P6eImageCodecDecryptor.prototype.run = function (data) {
    return P6E_ICE.CR.execute(data);
};

P6eImageCodecDecryptor.AES.prototype.wordArrayToUint8Array = function (wordArray) {
    const length = wordArray.words.length;
    const uint8Array = new Uint8Array(length << 2);
    let word, i, offset = 0;
    for (i = 0; i < length; i++) {
        word = wordArray.words[i];
        uint8Array[offset++] = word >> 24;
        uint8Array[offset++] = (word >> 16) & 0xff;
        uint8Array[offset++] = (word >> 8) & 0xff;
        uint8Array[offset++] = word & 0xff;
    }
    return uint8Array;
}

P6eImageCodecDecryptor.AES.prototype.execute = function (secret, data) {
    const ciphertext = CryptoJS.AES.encrypt(CryptoJS.lib.WordArray.create(data),
        CryptoJS.enc.Utf8.parse(secret), {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7,
        }).ciphertext;
    return this.wordArrayToUint8Array(ciphertext);
};

P6eImageCodecDecryptor.MD5.prototype.execute = function (data) {
    return CryptoJS.MD5(data).toString();
};

P6eImageCodecDecryptor.UUID.prototype.execute = function () {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
};

P6eImageCodecDecryptor.Compiler.key = function (id, version) {
    return "ID@" + id + "_VERSION@" + version;
}

P6eImageCodecDecryptor.Compiler.intToBytesLittle = function (value) {
    return [(value & 255), (value >> 8 & 255), (value >> 16 & 255), (value >> 24 & 255)]
};

P6eImageCodecDecryptor.Compiler.stringToBytes = function (value) {
    let c;
    const bytes = [];
    const length = value.length;
    for (let i = 0; i < length; i++) {
        c = value.charCodeAt(i);
        if (c >= 0x010000 && c <= 0x10FFFF) {
            bytes.push(((c >> 18) & 0x07) | 0xF0);
            bytes.push(((c >> 12) & 0x3F) | 0x80);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if (c >= 0x000800 && c <= 0x00FFFF) {
            bytes.push(((c >> 12) & 0x0F) | 0xE0);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if (c >= 0x000080 && c <= 0x0007FF) {
            bytes.push(((c >> 6) & 0x1F) | 0xC0);
            bytes.push((c & 0x3F) | 0x80);
        } else {
            bytes.push(c & 0xFF);
        }
    }
    return bytes;
};

P6eImageCodecDecryptor.Compiler.c1 = function (number) {
    const m = P6E_ICE.MD5.execute(number);
    return {
        enable: true,
        id: P6eImageCodecDecryptor.Compiler.c1.id,
        version: P6eImageCodecDecryptor.Compiler.c1.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecDecryptor.Compiler.c1.id = 1;
P6eImageCodecDecryptor.Compiler.c1.version = 1;

P6eImageCodecDecryptor.Compiler.c2 = function (number) {
    this.id = 2;
    this.version = 1;
    const m = P6E_ICE.MD5.execute(P6E_ICE.MD5.execute(number));
    return {
        enable: true,
        id: P6eImageCodecDecryptor.Compiler.c2.id,
        version: P6eImageCodecDecryptor.Compiler.c2.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecDecryptor.Compiler.c2.id = 2;
P6eImageCodecDecryptor.Compiler.c2.version = 1;

P6eImageCodecDecryptor.Compiler.prototype.init = function () {
    this.map = {};
    this.list = [];
    const keys = Object.keys(P6eImageCodecDecryptor.Compiler);
    for (let i = 0; i < keys.length; i++) {
        const key = keys[i];
        if (key.startsWith('c')) {
            const value = P6eImageCodecDecryptor.Compiler[key];
            this.list.push(value);
            this.map[P6eImageCodecDecryptor.Compiler.key(value.id, value.version)] = value;
        }
    }
};

P6eImageCodecDecryptor.Compiler.prototype.get = function () {
    if (this.list.length <= 0) {
        throw Error();
    } else {
        return this.list[Math.floor(Math.random() * this.list.length)];
    }
}

P6eImageCodecDecryptor.Compiler.prototype.execute = function (data) {
    const m = this.get()(P6E_ICE.NG.execute());
    const eData = P6E_ICE.AES.execute(m.secret, data);
    const r = [];
    if (m.enable) {
        r.push(1);
        r.push(...(P6eImageCodecDecryptor.Compiler.intToBytesLittle(m.id)));
        r.push(...(P6eImageCodecDecryptor.Compiler.intToBytesLittle(m.version)));
        if (m.other) {
            r.push(1);
            r.push(...P6eImageCodecDecryptor.Compiler.intToBytesLittle(m.other.length));
            for (let i = 0; i < m.other.length; i++) {
                const bytes = P6eImageCodecDecryptor.Compiler.stringToBytes(m.other[i]);
                r.push(...P6eImageCodecDecryptor.Compiler.intToBytesLittle(bytes.length));
                r.push(...bytes);
            }
        } else {
            r.push(0);
        }
        r.push(...P6eImageCodecDecryptor.Compiler.stringToBytes(m.number));
    } else {
        r.push(0);
    }
    r.push(...eData);
    return new Uint8Array(r);
};


P6eImageCodecDecryptor.NumberGenerator.prototype.execute = function () {
    return P6E_ICE.MD5.execute(P6E_ICE.UUID.execute().replace(/-/g, ''));
};

const P6E_ICE = new P6eImageCodecDecryptor();
P6E_ICE.AES = new P6eImageCodecDecryptor.AES();
P6E_ICE.MD5 = new P6eImageCodecDecryptor.MD5();
P6E_ICE.UUID = new P6eImageCodecDecryptor.UUID();
P6E_ICE.CR = new P6eImageCodecDecryptor.Compiler();
P6E_ICE.NG = new P6eImageCodecDecryptor.NumberGenerator();
P6E_ICE.CR.init();
window.P6E_ICE = P6E_ICE;


console.log(P6E_ICE.run(new Uint8Array([1, 2, 3])));
