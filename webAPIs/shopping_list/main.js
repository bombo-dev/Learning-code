const footerBtn = document.querySelector('.footer__button');
const input = document.querySelector('.footer__input');
const items = document.querySelector('.items');

console.log(footerBtn);
console.log(input);
console.log(input.value);

function addItem() {
  const value = input.value;

  if (value === '') {
    input.focus();
    return;
  }

  const newItem = document.createElement('div');
  newItem.setAttribute('class', 'item__row');

  const itemName = document.createElement('span');
  itemName.setAttribute('class', 'item__name');
  itemName.textContent = `${value}`;

  const deleteButton = document.createElement('button');
  deleteButton.setAttribute('class', 'item__delete');
  deleteButton.innerHTML = `<i class="fa-solid fa-trash"></i>`;

  const divider = document.createElement('div');
  divider.setAttribute('class', 'item__divider');

  newItem.append(itemName, deleteButton);
  items.append(newItem, divider);

  deleteButton.addEventListener('click', (e) => {
    newItem.remove();
    divider.remove();
  });

  input.value = '';
  input.focus();
}

window.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') {
    addItem();
  }
});

footerBtn.addEventListener('click', (e) => {
  addItem();
});
