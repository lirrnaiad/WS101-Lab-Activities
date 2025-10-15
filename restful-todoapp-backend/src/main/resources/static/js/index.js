const URL = "http://localhost:8080";

let editState = {
    modal: null,
    form: null,
    desc: null,
    date: null,
    user: null,
    cancel: null,
    backdrop: null,
    currentTodo: null
};

// Helpers
function getActiveUsername() {
    const userInput = document.getElementById('task-user-input');
    return (userInput?.value || 'darshan').trim();
}

function getSelectedFilter() {
    const sel = document.getElementById('user-filter');
    return sel?.value || '*';
}

function ensureEditModal() {
    if (document.getElementById('edit-modal')) return;

    const style = document.createElement('style');
    style.id = 'edit-modal-styles';
    style.textContent = `
      #edit-modal { position: fixed; inset: 0; z-index: 1000; display: none; }
      #edit-modal.show { display: block; }
      #edit-modal .backdrop {
        position: absolute; inset: 0; background: rgba(0,0,0,0.55); backdrop-filter: blur(2px);
      }
      #edit-modal .dialog {
        position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);
        width: min(560px, 92vw); background: var(--panel); border: 1px solid var(--border);
        border-radius: 12px; box-shadow: 0 18px 48px rgba(0,0,0,0.45);
        padding: 18px; color: var(--text);
      }
      #edit-modal .title { font-weight: 700; margin-bottom: 12px; letter-spacing: .2px; }
      #edit-modal .form-grid { display: grid; gap: 12px; }
      #edit-modal label { font-size: 0.92rem; opacity: 0.9; }
      #edit-modal input[type="text"], #edit-modal input[type="date"] {
        width: 100%; min-height: 42px; background: var(--input); color: var(--text);
        padding: 10px 12px; border-radius: 10px; border: 1px solid transparent;
        outline: none; transition: background .2s, border-color .2s, box-shadow .2s;
      }
      #edit-modal input[type="text"]:focus, #edit-modal input[type="date"]:focus {
        border-color: var(--primary); box-shadow: 0 0 0 3px rgba(158,120,207,0.35);
      }
      #edit-modal .actions {
        display: flex; gap: 10px; justify-content: flex-end; margin-top: 8px;
      }
      #edit-modal .btn {
        min-width: 96px; height: 38px; border-radius: 10px; border: 1px solid transparent;
        padding: 0 14px; font-weight: 600; transition: transform .08s, background-color .2s, box-shadow .2s;
      }
      #edit-modal .btn:active { transform: scale(0.98); }
      #edit-modal .btn-primary {
        background: var(--primary); color: #fff;
      }
      #edit-modal .btn-primary:hover {
        background: var(--primary-soft); box-shadow: 0 6px 18px rgba(158,120,207,0.18);
        cursor: pointer;
      }
      #edit-modal .btn-ghost {
        background: rgba(255,255,255,0.02); color: var(--text); border: 1px solid rgba(255,255,255,0.06);
      }
      #edit-modal .btn-ghost:hover {
        background: rgba(255,255,255,0.04); cursor: pointer;
      }
    `;
    document.head.appendChild(style);

    const modal = document.createElement('div');
    modal.id = 'edit-modal';
    modal.innerHTML = `
      <div class="backdrop" data-close="true"></div>
      <div class="dialog" role="dialog" aria-modal="true" aria-labelledby="edit-title">
        <div class="title" id="edit-title">Edit task</div>
        <form id="edit-form" class="form-grid">
          <div>
            <label for="edit-desc">Description</label>
            <input id="edit-desc" type="text" placeholder="Task description" />
          </div>
          <div>
            <label for="edit-date">Date</label>
            <input id="edit-date" type="date" />
          </div>
          <div>
            <label for="edit-user">User</label>
            <input id="edit-user" type="text" placeholder="Username" />
          </div>
          <div class="actions">
            <button type="button" class="btn btn-ghost" id="edit-cancel">Cancel</button>
            <button type="submit" class="btn btn-primary" id="edit-save">Save</button>
          </div>
        </form>
      </div>
    `;
    document.body.appendChild(modal);

    editState.modal = modal;
    editState.form = modal.querySelector('#edit-form');
    editState.desc = modal.querySelector('#edit-desc');
    editState.date = modal.querySelector('#edit-date');
    editState.user = modal.querySelector('#edit-user');
    editState.cancel = modal.querySelector('#edit-cancel');
    editState.backdrop = modal.querySelector('.backdrop');

    editState.cancel.addEventListener('click', closeEditModal);
    editState.backdrop.addEventListener('click', (e) => {
        if (e.target.dataset.close === 'true') closeEditModal();
    });
    window.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && editState.modal.classList.contains('show')) closeEditModal();
    });

    // Save handler
    editState.form.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!editState.currentTodo) return;

        const description = editState.desc.value.trim();
        const targetDate = editState.date.value.trim();
        const username = editState.user.value.trim() || getActiveUsername();

        if (!description || !targetDate) {
            alert('Please fill in description and date.');
            return;
        }

        try {
            const userInput = document.getElementById('task-user-input');
            if (userInput) userInput.value = username;

            const payload = {
                id: editState.currentTodo.id,
                username,
                description,
                targetDate,
                done: !!editState.currentTodo.done
            };

            await updateTodo(username, editState.currentTodo.id, payload);
            await populateUsersFilter(true);
            await refreshListAccordingToFilter();
            closeEditModal();
        } catch (err) {
            console.error(err);
            alert('Failed to save changes.');
        }
    });
}

function openEditModal(todo) {
    ensureEditModal();
    editState.currentTodo = { ...todo };

    editState.desc.value = todo.description ?? '';
    editState.user.value = (todo.username ?? getActiveUsername()).trim();
    editState.date.value = todo.targetDate ? `${todo.targetDate}` : '';

    editState.modal.classList.add('show');
    editState.modal.setAttribute('aria-hidden', 'false');
    editState.desc.focus();
}

function closeEditModal() {
    if (!editState.modal) return;
    editState.modal.classList.remove('show');
    editState.modal.setAttribute('aria-hidden', 'true');
    editState.currentTodo = null;
}

// API Calls
async function fetchTodos(username) {
    const res = await fetch(`${URL}/users/${encodeURIComponent(username)}/todos`);
    if (!res.ok) throw new Error('Failed to fetch to-dos');
    return res.json();
}

async function fetchAllTodos() {
    const res = await fetch(`${URL}/todos`);
    if (!res.ok) throw new Error('Failed to fetch to-dos');
    return res.json();
}

async function addTodo(username, todo) {
    const res = await fetch(`${URL}/users/${encodeURIComponent(username)}/todos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(todo)
    });
    if (!res.ok) throw new Error('Failed to add to-do');
    return res.json();
}

async function updateTodo(username, id, todo) {
    const res = await fetch(`${URL}/users/${encodeURIComponent(username)}/todos/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(todo)
    });
    if (!res.ok) throw new Error('Failed to update to-do');
    return res.json();
}

async function deleteTodo(username, id) {
    const res = await fetch(`${URL}/users/${encodeURIComponent(username)}/todos/${encodeURIComponent(id)}`, {
        method: 'DELETE'
    });
    if (!res.ok) throw new Error('Failed to delete to-do');
}

async function fetchUsers() {
    const res = await fetch(`${URL}/users`);
    if (!res.ok) throw new Error('Failed to fetch users');
    return res.json();
}

async function populateUsersFilter(preserveSelection = true) {
    const sel = document.getElementById('user-filter');
    if (!sel) return;
    const prev = preserveSelection ? sel.value : '*';

    let users = [];
    try {
        users = await fetchUsers();
    } catch (e) {
        console.error(e);
    }

    const seen = new Set();
    sel.innerHTML = '';

    const allOpt = document.createElement('option');
    allOpt.value = '*';
    allOpt.textContent = 'All users';
    sel.appendChild(allOpt);

    users
        .filter(u => typeof u === 'string')
        .sort((a, b) => a.localeCompare(b))
        .forEach(u => {
            if (seen.has(u)) return;
            seen.add(u);
            const opt = document.createElement('option');
            opt.value = u;
            opt.textContent = u;
            sel.appendChild(opt);
        });

    if ([...sel.options].some(o => o.value === prev)) sel.value = prev;
}

async function refreshListAccordingToFilter() {
    const selected = getSelectedFilter();
    const todos = selected === '*' ? await fetchAllTodos() : await fetchTodos(selected);
    renderTodos(todos);
}

// UI Renders
function renderTodos(todos) {
    const todoList = document.getElementById('to-do-list');
    const doneList = document.getElementById('done-list');
    const todoTracker = document.getElementById('todo-tracker');
    const doneTracker = document.getElementById('done-tracker');
    if (!todoList || !doneList) return;

    // counts
    const todoCount = todos.filter(t => !t.done).length;
    const doneCount = todos.filter(t => t.done).length;

    if (todoTracker) todoTracker.textContent = `Tasks to do - ${todoCount}`;
    if (doneTracker) doneTracker.textContent = `Done - ${doneCount}`;

    todoList.innerHTML = '';
    doneList.innerHTML = '';

    todos.forEach(t => {
        const li = document.createElement('li');
        const description = t.description ?? '';
        const username = t.username ?? '';
        const targetDate = t.targetDate ? new Date(`${t.targetDate}T00:00:00`).toLocaleDateString() : '';

        // done item
        if (t.done) {
            li.className = 'done';
            li.innerHTML = `
                <span class="description">${description}</span>
                <span class="task-date">
                    <img src="images/icons/date.png" alt="Calendar">
                    ${targetDate}
                </span>
                <span class="task-user">
                    <img src="images/icons/user.png" alt="User">
                    ${username}
                </span>
            `;
            doneList.appendChild(li);
            return;
        }

        // to-do item
        li.className = 'to-do';
        li.innerHTML = `
            <span class="description">${description}</span>
            <span class="task-date">
                <img src="/images/icons/date.png" alt="Calendar">
                ${targetDate}
            </span>
            <span class="task-user">
                <img src="/images/icons/user.png" alt="User">
                ${username}
            </span>
            <div class="actions">
                <button class="done-button" title="Mark done">
                    <img src="/images/icons/done.png" alt="Done">
                </button>
                <button class="edit-button" title="Edit">
                    <img src="/images/icons/edit.png" alt="Edit">
                </button>
                <button class="remove-button" title="Remove">
                    <img src="/images/icons/remove.png" alt="Delete">
                </button>
            </div>
        `;

        // Done action
        const doneBtn = li.querySelector('.done-button');
        if (doneBtn) {
            doneBtn.addEventListener('click', async () => {
                try {
                    const payload = {
                        id: t.id,
                        username: username || t.username || getActiveUsername(),
                        description: t.description,
                        targetDate: t.targetDate,
                        done: true
                    };
                    await updateTodo(payload.username, t.id, payload);
                    await refreshListAccordingToFilter();
                } catch (e) {
                    console.error(e);
                    alert('Failed to mark task as done.');
                }
            });
        }

        // Edit action
        const editBtn = li.querySelector('.edit-button');
        if (editBtn) {
            editBtn.addEventListener('click', () => openEditModal(t));
        }

        todoList.appendChild(li);

        // Remove action
        const removeBtn = li.querySelector('.remove-button');
        if (removeBtn) {
            removeBtn.addEventListener('click', async () => {
                const confirmed = confirm(`Delete this task?\n\n"${description}"`);
                if (!confirmed) return;
                try {
                    const pathUser = t.username || getActiveUsername();
                    await deleteTodo(pathUser, t.id);
                    await refreshListAccordingToFilter();
                } catch (e) {
                    console.error(e);
                    alert('Failed to remove task.');
                }
            });
        }
    });
}

// Handlers
async function handleAddTodo() {
    const descriptionInput = document.getElementById('task-input');
    const dateInput = document.getElementById('task-date-input');
    const userInput = document.getElementById('task-user-input');

    if (!descriptionInput || !dateInput || !userInput) return;

    const description = descriptionInput.value.trim();
    const targetDate = dateInput.value.trim(); // YYYY-MM-DD
    const username = (userInput.value || 'darshan').trim();

    if (!description || !targetDate) {
        alert('Please fill in description and date.');
        return;
    }

    const newTodo = { description, targetDate, done: false };

    try {
        await addTodo(username, newTodo);
        await populateUsersFilter(true);
        await refreshListAccordingToFilter();
        descriptionInput.value = '';
        dateInput.value = '';
    } catch (error) {
        console.error(error);
        alert(error.message || 'Failed to add task.');
    }
}

// Initialization
document.addEventListener('DOMContentLoaded', async () => {
    ensureEditModal();

    const addBtn = document.getElementById('add-task-button');
    if (addBtn) addBtn.addEventListener('click', handleAddTodo);

    try {
        await populateUsersFilter(true);
        const filterSel = document.getElementById('user-filter');
        if (filterSel) {
            filterSel.addEventListener('change', () => {
                refreshListAccordingToFilter().catch(console.error);
            });
        }
        await refreshListAccordingToFilter();
    } catch (e) {
        console.error(e);
    }
});
