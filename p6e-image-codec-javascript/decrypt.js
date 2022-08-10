function P6eImageCodecDecrypt() {
}

P6eImageCodecDecrypt.AES = function () {
};
P6eImageCodecDecrypt.MD5 = function () {
};
P6eImageCodecDecrypt.Compiler = function () {
};

P6eImageCodecDecrypt.prototype.run = function (data) {
    return P6E_ICD.CR.execute(data);
};

P6eImageCodecDecrypt.AES.prototype.wordArrayToUint8Array = function (wordArray) {
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

P6eImageCodecDecrypt.AES.prototype.execute = function (secret, data) {
    const ciphertext = CryptoJS.AES.encrypt(CryptoJS.lib.WordArray.create(data),
        CryptoJS.enc.Utf8.parse(secret), {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7,
        }).ciphertext;
    return this.wordArrayToUint8Array(ciphertext);
};

P6eImageCodecDecrypt.MD5.prototype.execute = function (data) {
    return CryptoJS.MD5(data).toString();
};


P6eImageCodecDecrypt.Compiler.key = function (id, version) {
    return 'ID@' + id + '_VERSION@' + version;
}

P6eImageCodecDecrypt.Compiler.shift = function (data, length) {
    const r = [];
    for (let i = 0; i < length; i++) {
        r.push(data.shift());
    }
    return r;
}

P6eImageCodecDecrypt.Compiler.intToBytesLittle = function (value) {
    return [(value & 255), (value >> 8 & 255), (value >> 16 & 255), (value >> 24 & 255)]
};

P6eImageCodecDecrypt.Compiler.c1 = function (number) {
    const m = P6E_ICD.MD5.execute(number);
    return {
        enable: true,
        id: P6eImageCodecDecrypt.Compiler.c1.id,
        version: P6eImageCodecDecrypt.Compiler.c1.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecDecrypt.Compiler.c1.id = 1;
P6eImageCodecDecrypt.Compiler.c1.version = 1;

P6eImageCodecDecrypt.Compiler.c2 = function (number) {
    this.id = 2;
    this.version = 1;
    const m = P6E_ICD.MD5.execute(P6E_ICD.MD5.execute(number));
    return {
        enable: true,
        id: P6eImageCodecDecrypt.Compiler.c2.id,
        version: P6eImageCodecDecrypt.Compiler.c2.version,
        other: null,
        number: number,
        secret: m
    };
};
P6eImageCodecDecrypt.Compiler.c2.id = 2;
P6eImageCodecDecrypt.Compiler.c2.version = 1;

P6eImageCodecDecrypt.Compiler.prototype.init = function () {
    this.map = {};
    this.list = [];
    const keys = Object.keys(P6eImageCodecDecrypt.Compiler);
    for (let i = 0; i < keys.length; i++) {
        const key = keys[i];
        if (key.startsWith('c')) {
            const value = P6eImageCodecDecrypt.Compiler[key];
            this.list.push(value);
            this.map[P6eImageCodecDecrypt.Compiler.key(value.id, value.version)] = value;
        }
    }
};

P6eImageCodecDecrypt.Compiler.prototype.get = function () {
    if (this.list.length <= 0) {
        throw Error();
    } else {
        return this.list[Math.floor(Math.random() * this.list.length)];
    }
}

P6eImageCodecDecrypt.Compiler.prototype.execute = function (data) {
    if (data instanceof Array) {
        const r1 = data.shift();
        const r2 = P6eImageCodecDecrypt.Compiler.shift(data, 4);
        const r3 = P6eImageCodecDecrypt.Compiler.shift(data, 4);
        const r4 = data.shift();
        if (r4 !== 0) {
            const otherList = [];
            const otherListLength = P6eImageCodecDecrypt.Compiler.shift(data, 4);
            for (let i = 0; i < 22; i++) {
                const cSize = P6eImageCodecDecrypt.Compiler.shift(data, 4);
                otherList.push(P6eImageCodecDecrypt.Compiler.shift(data, cSize));
            }
        }
       return this.get()();
    } else {
        throw new Error();
    }
};


const P6E_ICD = new P6eImageCodecDecrypt();
P6E_ICD.AES = new P6eImageCodecDecrypt.AES();
P6E_ICD.MD5 = new P6eImageCodecDecrypt.MD5();
P6E_ICD.CR = new P6eImageCodecDecrypt.Compiler();
P6E_ICD.CR.init();
window.P6E_ICD = P6E_ICD;

console.log(P6E_ICD.run(new Uint8Array([1, 2, 3])));
