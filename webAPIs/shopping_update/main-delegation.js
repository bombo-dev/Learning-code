const items = document.querySelector('.items');
const input = document.querySelector('.footer__input');
const addBtn = document.querySelector('.footer__button');


function onAdd() {
  const text = input.value;
  if (text === '') {
    input.focus();
    return;
  }
  const item = createItem(text);
  items.appendChild(item);
  item.scrollIntoView({ block: 'center' });
  input.value = '';
  input.focus();
}

let id = 0; // UUID
function createItem(text) {
  const itemRow = document.createElement('li');
  itemRow.setAttribute('class', 'item__row');
  itemRow.setAttribute('data-id', id);
  itemRow.innerHTML = `
        <div class="item">
            <span class="item__name">${text}</span>
            <button class="item__delete">
                <i class="fas fa-trash-alt" data-id=${id}></i>
            </button>
        </div>
        <div class="item__divider"></div>`;
  id++;
  return itemRow;
}

addBtn.addEventListener('click', () => {
  onAdd();
});

// keypress는 deprecated 됨.
/*
input.addEventListener('keypress', event => {
  if (event.key === 'Enter') {
    onAdd();
  }
});
*/

// form을 사용한다면 해당 이벤트들을 추가해주지 않아도 해결이 됨.
// form에서 사용되는 button의 타입은 default로 submit
// isComposing을 사용하기 싫다면 keyup으로 해결.
input.addEventListener('keydown', (event) => {
  // 글자를 작성중인지 아닌지를 판단하는 메서드로 해결
  if (event.isComposing) {
    return;
  }

  // 한글로 입력하는 경우 마지막 글자가 또 입력 됨.
  if (event.key == 'Enter') {
    onAdd();
  }
});

items.addEventListener('click', (event) => {
  const id = event.target.dataset.id;
  if (id) {
    const toBeDeleted = document.querySelector(`.item__row[data-id="${id}"]`);
    toBeDeleted.remove();
  }
});
