function P6eImageCodecEncryptor() {
}

P6eImageCodecEncryptor.AES = function () {
};
P6eImageCodecEncryptor.MD5 = function () {
};
P6eImageCodecEncryptor.UUID = function () {
};
P6eImageCodecEncryptor.Compiler = function () {
};
P6eImageCodecEncryptor.NumberGenerator = function () {
};

P6eImageCodecEncryptor.prototype.run = function (data) {
    return P6E_ICE.CR.execute(data);
};

P6eImageCodecEncryptor.AES.prototype.execute = function (secret, data) {
    return CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse('LDS'),
        CryptoJS.enc.Utf8.parse(secret), {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7}).toString(CryptoJS.enc.Hex);
};

P6eImageCodecEncryptor.MD5.prototype.execute = function (data) {
    return CryptoJS.MD5(data).toString();
};

P6eImageCodecEncryptor.UUID.prototype.execute = function () {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
};

P6eImageCodecEncryptor.Compiler.key = function (id, version) {
    return "ID@" + id + "_VERSION@" + version;
}

P6eImageCodecEncryptor.Compiler.intToBytesLittle = function (value) {
    return [(value & 255), (value >> 8 & 255), (value >> 16 & 255), (value >> 24 & 255)]
};

P6eImageCodecEncryptor.Compiler.stringToBytes = function (value) {
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

P6eImageCodecEncryptor.Compiler.c1 = function (number) {
    const m = P6E_ICE.MD5.execute(number);
    return {
        enable: true,
        id: P6eImageCodecEncryptor.Compiler.c1.id,
        version: P6eImageCodecEncryptor.Compiler.c1.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecEncryptor.Compiler.c1.id = 1;
P6eImageCodecEncryptor.Compiler.c1.version = 1;

P6eImageCodecEncryptor.Compiler.c2 = function (number) {
    this.id = 2;
    this.version = 1;
    const m = P6E_ICE.MD5.execute(P6E_ICE.MD5.execute(number));
    return {
        enable: true,
        id: P6eImageCodecEncryptor.Compiler.c2.id,
        version: P6eImageCodecEncryptor.Compiler.c2.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecEncryptor.Compiler.c2.id = 2;
P6eImageCodecEncryptor.Compiler.c2.version = 1;

P6eImageCodecEncryptor.Compiler.prototype.init = function () {
    this.map = {};
    this.list = [];
    const keys = Object.keys(P6eImageCodecEncryptor.Compiler);
    for (let i = 0; i < keys.length; i++) {
        const key = keys[i];
        if (key.startsWith('c')) {
            const value = P6eImageCodecEncryptor.Compiler[key];
            this.list.push(value);
            this.map[P6eImageCodecEncryptor.Compiler.key(value.id, value.version)] = value;
        }
    }
};

P6eImageCodecEncryptor.Compiler.prototype.get = function () {
    if (this.list.length <= 0) {
        throw Error();
    } else {
        return this.list[Math.floor(Math.random() * this.list.length)];
    }
}

P6eImageCodecEncryptor.Compiler.prototype.execute = function (data) {
    const m = this.get()(P6E_ICE.NG.execute());
    const eData = P6E_ICE.AES.execute(m.secret, data);
    console.log('x::::' + eData);
    const r = [];
    if (m.enable) {
        r.push(1);
        r.push(...(P6eImageCodecEncryptor.Compiler.intToBytesLittle(m.id)));
        r.push(...(P6eImageCodecEncryptor.Compiler.intToBytesLittle(m.version)));
        if (m.other) {
            r.push(1);
            r.push(...P6eImageCodecEncryptor.Compiler.intToBytesLittle(m.other.length));
            for (let i = 0; i < m.other.length; i++) {
                const bytes = P6eImageCodecEncryptor.Compiler.stringToBytes(m.other[i]);
                r.push(...P6eImageCodecEncryptor.Compiler.intToBytesLittle(bytes.length));
                r.push(...bytes);
            }
        } else {
            r.push(0);
        }
        r.push(...P6eImageCodecEncryptor.Compiler.stringToBytes(m.number));
    } else {
        r.push(0);
    }
    r.push(...eData);
    return r;
};


P6eImageCodecEncryptor.NumberGenerator.prototype.execute = function () {
    return P6E_ICE.MD5.execute(P6E_ICE.UUID.execute().replace(/-/g, ''));
};

const P6E_ICE = new P6eImageCodecEncryptor();
P6E_ICE.AES = new P6eImageCodecEncryptor.AES();
P6E_ICE.MD5 = new P6eImageCodecEncryptor.MD5();
P6E_ICE.UUID = new P6eImageCodecEncryptor.UUID();
P6E_ICE.CR = new P6eImageCodecEncryptor.Compiler();
P6E_ICE.NG = new P6eImageCodecEncryptor.NumberGenerator();
P6E_ICE.CR.init();
window.P6E_ICE = P6E_ICE;


console.log(P6E_ICE.run([1, 2, 3]));
