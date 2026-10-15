function confirmarExclusao(form) {
    const titulo = form.dataset.titulo;

    return confirm(
        `Deseja realmente excluir o desafio "${titulo}"?`
    );
}

function confirmarExclusaoQuestao(form) {
    const titulo = form.dataset.titulo;

    return confirm(
        `Deseja realmente excluir a questão "${titulo}"?`
    );
}