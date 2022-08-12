function P6eImageCodecDecrypt() {
}

P6eImageCodecDecrypt.AES = function () {
};
P6eImageCodecDecrypt.MD5 = function () {
};
P6eImageCodecDecrypt.Compiler = function () {
};

P6eImageCodecDecrypt.prototype.run = function (data) {
  return P6E_ICD.COMPILER.execute(data);
};

P6eImageCodecDecrypt.AES.IV_DEFAULT = '00000000';
P6eImageCodecDecrypt.AES.transformUint8ArrayToWordArray = function (data) {
  let index = 0;
  const result = [];
  const dLength = data.length;
  const rLength = Math.ceil(dLength / 4);
  for (let i = 0; i < rLength; i++) {
    result.push((((data[index++] || 0) & 0xff) << 24) | (((data[index++] || 0) & 0xff) << 16) | (((data[index++] || 0) & 0xff) << 8) | ((data[index++] || 0) & 0xff));
  }
  return CryptoJS.lib.WordArray.create(result, dLength);
}

P6eImageCodecDecrypt.AES.transformWordArrayToUint8Array = function (data) {
  const bytesLength = data.sigBytes;
  const dataLength = data.words.length;
  const uint8Array = new Uint8Array(bytesLength);
  let word, offset = 0;
  for (let i = 0; i < dataLength; i++) {
    word = data.words[i];
    if (offset < bytesLength) {
      uint8Array[offset++] = word >> 24;
    }
    if (offset < bytesLength) {
      uint8Array[offset++] = (word >> 16) & 0xff;
    }
    if (offset < bytesLength) {
      uint8Array[offset++] = (word >> 8) & 0xff;
    }
    if (offset < bytesLength) {
      uint8Array[offset++] = word & 0xff;
    }
  }
  return uint8Array;
}

P6eImageCodecDecrypt.AES.prototype.execute = function (secret, data) {
  const result = CryptoJS.AES.decrypt({ciphertext: P6eImageCodecDecrypt.AES.transformUint8ArrayToWordArray(data)}, CryptoJS.enc.Utf8.parse(secret), {
    iv: CryptoJS.enc.Utf8.parse(P6eImageCodecDecrypt.AES.IV_DEFAULT),
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return P6eImageCodecDecrypt.AES.transformWordArrayToUint8Array(result);
};

P6eImageCodecDecrypt.MD5.prototype.execute = function (data) {
  return CryptoJS.MD5(data).toString();
};


P6eImageCodecDecrypt.Compiler.key = function (id, version) {
  return 'ID@' + id + '_VERSION@' + version;
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
        out += String.fromCharCode(((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
        break;
    }
  }
  return out;
};

P6eImageCodecDecrypt.Compiler.c1 = function (number) {
  return P6E_ICD.MD5.execute(number);
};
P6eImageCodecDecrypt.Compiler.c1.id = 1;
P6eImageCodecDecrypt.Compiler.c1.version = 1;

P6eImageCodecDecrypt.Compiler.c2 = function (number) {
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
    let index = 0;
    const r1 = data[index++];
    if (r1 !== 0) {
      let otherList = null;
      const r2 = P6eImageCodecDecrypt.Compiler.bytesToIntLittle([data[index++], data[index++], data[index++], data[index++]]);
      const r3 = P6eImageCodecDecrypt.Compiler.bytesToIntLittle([data[index++], data[index++], data[index++], data[index++]]);
      const r4 = data[index++];
      if (r4 !== 0) {
        otherList = [];
        const otherListLength = P6eImageCodecDecrypt.Compiler.bytesToIntLittle([data[index++], data[index++], data[index++], data[index++]]);
        for (let i = 0; i < otherListLength; i++) {
          const contentBytes = [];
          const contentLength = P6eImageCodecDecrypt.Compiler.bytesToIntLittle([data[index++], data[index++], data[index++], data[index++]]);
          for (let j = 0; j < contentLength.length; j++) {
            contentBytes.push(data[index++]);
          }
          otherList.push(P6eImageCodecDecrypt.Compiler.bytesToString(contentBytes));
        }
      }
      const numberLength = P6eImageCodecDecrypt.Compiler.bytesToIntLittle([data[index++], data[index++], data[index++], data[index++]]);
      const numberBytes = [];
      for (let j = 0; j < numberLength; j++) {
        numberBytes.push(data[index++]);
      }
      const r5 = P6eImageCodecDecrypt.Compiler.bytesToString(numberBytes);
      const secret = this.get({enable: r1 !== 0, id: r2, version: r3, other: otherList, number: r5})(r5);
      return P6E_ICD.AES.execute(secret, data.subarray(index));
    }
  } else {
    throw new Error();
  }
};

const P6E_ICD = new P6eImageCodecDecrypt();
P6E_ICD.AES = new P6eImageCodecDecrypt.AES();
P6E_ICD.MD5 = new P6eImageCodecDecrypt.MD5();
P6E_ICD.COMPILER = new P6eImageCodecDecrypt.Compiler();
P6E_ICD.COMPILER.init();
window.P6E_ICD = P6E_ICD;
