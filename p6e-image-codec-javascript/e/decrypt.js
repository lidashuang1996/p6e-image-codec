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
  console.log('mm www  ', data, CryptoJS.lib.WordArray.create(data));
  console.log(CryptoJS.enc.Utf8.parse(secret));
  console.log(CryptoJS.AES.decrypt(CryptoJS.lib.WordArray.create(data),
    CryptoJS.enc.Utf8.parse(secret), {
      iv: CryptoJS.enc.Utf8.parse('00000000'),
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    }));
  // const uint8Array = this.wordArrayToUint8Array(CryptoJS.AES.decrypt(CryptoJS.lib.WordArray.create(data),
  //   CryptoJS.enc.Utf8.parse(secret), { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 }));
  // console.log(uint8Array);
  // return this.wordArrayToUint8Array(ciphertext);
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

P6eImageCodecDecrypt.Compiler.bytesToIntLittle = function (bytes) {
  return bytes[0] & 255 | (bytes[1] & 255) << 8 | (bytes[2] & 255) << 16 | (bytes[3] & 255) << 24;
};

P6eImageCodecDecrypt.Compiler.bytesToString = function (data) {
  let out, i, len, c, char2, char3;
  const tempAry = data;
  out = '';
  len = tempAry.length;
  i = 0;
  while (i < len) {
    c = tempAry[i++];
    switch (c >> 4) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        // 0xxxxxxx
        out += String.fromCharCode(c);
        break;
      case 12:
      case 13:
        // 110x xxxx   10xx xxxx
        char2 = tempAry[i++];
        out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
        break;
      case 14:
        // 1110 xxxx  10xx xxxx  10xx xxxx
        char2 = tempAry[i++];
        char3 = tempAry[i++];
        out += String.fromCharCode(((c & 0x0F) << 12) |
          ((char2 & 0x3F) << 6) |
          ((char3 & 0x3F) << 0));
        break;
    }
  }
  return out;
};

P6eImageCodecDecrypt.Compiler.c1 = function (number) {
  console.log('ddd 11 ' + number + '  ' + P6E_ICD.MD5.execute(number))
  return P6E_ICD.MD5.execute(number);
};
P6eImageCodecDecrypt.Compiler.c1.id = 1;
P6eImageCodecDecrypt.Compiler.c1.version = 1;

P6eImageCodecDecrypt.Compiler.c2 = function (number) {
  console.log('ddd 22 ' + number + '  ' + P6E_ICD.MD5.execute(P6E_ICD.MD5.execute(number)));
  return P6E_ICD.MD5.execute(P6E_ICD.MD5.execute(number));
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

P6eImageCodecDecrypt.Compiler.prototype.get = function (data) {
  const keys = Object.keys(this.map);
  if (keys.length <= 0) {
    throw Error();
  } else {
    const result = this.map[P6eImageCodecDecrypt.Compiler.key(data.id, data.version)];
    if (result == null) {
      throw Error();
    } else {
      return result;
    }
  }
}

P6eImageCodecDecrypt.Compiler.prototype.execute = function (data) {
  if (data instanceof Uint8Array) {
    data = Array.from(data);
    const r1 = data.shift();
    if (r1 !== 0) {
      let otherList = null;
      const r2 = P6eImageCodecDecrypt.Compiler.bytesToIntLittle(P6eImageCodecDecrypt.Compiler.shift(data, 4));
      const r3 = P6eImageCodecDecrypt.Compiler.bytesToIntLittle(P6eImageCodecDecrypt.Compiler.shift(data, 4));
      const r4 = data.shift();
      if (r4 !== 0) {
        otherList = [];
        const otherListLength = P6eImageCodecDecrypt.Compiler.bytesToIntLittle(P6eImageCodecDecrypt.Compiler.shift(data, 4));
        for (let i = 0; i < otherListLength; i++) {
          const contentLength = P6eImageCodecDecrypt.Compiler.bytesToIntLittle(P6eImageCodecDecrypt.Compiler.shift(data, 4));
          otherList.push(P6eImageCodecDecrypt.Compiler.shift(data, contentLength));
        }
      }
      const r5 = P6eImageCodecDecrypt.Compiler.bytesToString(P6eImageCodecDecrypt.Compiler.shift(data, 32));
      console.log(' r5  ' + r5);
      const secret = this.get({enable: r1 !== 0, id: r2, version: r3, other: otherList, number: r5})(r5);
      console.log(' secret  ' + secret);
      return P6E_ICD.AES.execute(secret, new Uint8Array(data));
    }
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

// console.log(P6E_ICD.run(new Uint8Array([1,2,0,0,0,1,0,0,0,0,101,100,48,48,53,50,57,99,53,98,56,99,56,55,54,52,48,97,99,48,53,101,102,54,57,57,102,53,53,49,53,50,105,254,54,14,117,194,211,116,20,152,125,228,54,209,245,239])));

console.log(P6E_ICD.run(window.rr));
