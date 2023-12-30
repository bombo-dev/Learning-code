const footerBtn = document.querySelector('.footer__button');
const input = document.querySelector('.footer__input');
const items = document.querySelector('.items');

function addItem() {
  const value = input.value;

  if (value === '') {
    input.focus();
    return;
  }

  const itemRow = createItem(value);
  items.append(itemRow);

  input.value = '';
  input.focus();
}

function createItem(text) {
  const newItemRow = document.createElement('div');
  newItemRow.setAttribute('class', 'item__row');

  const newItem = document.createElement('div');
  newItem.setAttribute('class', 'item');

  const itemName = document.createElement('span');
  itemName.setAttribute('class', 'item__name');
  itemName.textContent = `${text}`;

  const deleteButton = document.createElement('button');
  deleteButton.setAttribute('class', 'item__delete');
  deleteButton.innerHTML = `<i class="fa-solid fa-trash"></i>`;

  const divider = document.createElement('div');
  divider.setAttribute('class', 'item__divider');

  newItem.append(itemName, deleteButton);
  newItemRow.append(newItem, divider);

  deleteButton.addEventListener('click', (e) => {
    items.removeChild(newItemRow);
  });

  return newItemRow;
}

window.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') {
    addItem();
  }
});

footerBtn.addEventListener('click', (e) => {
  addItem();
});
