const a = function (max) {
    return {
        [Symbol.iterator]() {
            let pre = 0, cur = 1;
            return {
                next() {
                    [pre, cur] = [cur, pre + cur];
                    return {done: cur > max, value: cur};
                }
            };
        }
    }
}

const b = function (max) {
    let [pre, cur] = [0, 1];
    return {
        [Symbol.iterator]() {
            return this
        },
        next() {
            [pre, cur] = [cur, pre + cur];
            return {done: cur > max, value: cur};
        }
    }
}

const c = b(10)

console.log(c.next());

for (const n of a(10)) {
    console.log(n);
}

console.log('-------------------');

for (const n of b(10)) {
    console.log(n);
}


console.log(...[1,2], ...[3,4])
