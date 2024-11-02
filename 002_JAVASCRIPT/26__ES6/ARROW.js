let a = (x, y) => x * y

let b  = (x, y) => { return  x * y }
console.log(a(2, 3))
console.log(b(2, 3))
const c = () => {const x = 1}

(function() {
    const bar = () => () => console.log(this)
    bar()()
}).call()


let d = (...rest) => {
    console.log(rest)
}

d(1, 2, 3)


const counter = {
    count: 0,
    inc: function() {
        this.count++
        console.log(this.count)
    }, // 일반 함수는 호출 순간에 this가 바인딩 된다. 그래서 객체를 앎.

    inc_arrow : () => {
        this.count++
        console.log(this.count)
    } // 화살표 함수는 선언 시점에 this가 바인딩 된다. 그래서 객체를 모름.
}

counter.inc()
counter.inc_arrow()

console.log(Math.pow(2, 63) - Math.pow(2, 57))
