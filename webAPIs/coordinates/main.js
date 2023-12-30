const vertical = document.querySelector('.vertical');
const horizontal = document.querySelector('.horizontal');
const target = document.querySelector('.target');
const coordinate = document.querySelector('.coordinate');
const targetSize = target.getBoundingClientRect();

document.addEventListener('mousemove', (event) => {
  const x = event.clientX;
  const y = event.clientY;

  const visualTargetWidth = targetSize.width / 2;
  const visualTargetHeight = targetSize.height / 2;

  /*
  left와 top은 layout부터 재 호출하므로 렌더링 성능이 저하.
  vertical.style.left = `${x}px`;
  horizontal.style.top = `${y}px`;
  target.style.left = `${x}px`;
  target.style.top = `${y}px`;
  */

  vertical.style.transform = `translateX(${x}px)`;
  horizontal.style.transform = `translateY(${y}px)`;
  target.style.transform = `translate(${x - visualTargetWidth}px, ${
    y - visualTargetHeight
  }px)`;
  coordinate.style.transform = `translate(${x}px, ${y}px)`;
  coordinate.innerHTML = `${x}px, ${y}px`;
});
