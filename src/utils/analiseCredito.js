// Formata valor em reais (ex: 5000 -> R$ 5.000,00)
export function formatarMoeda(valor) {
    const partes = valor.toFixed(2).split('.');
    const inteiro = partes[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return 'R$ ' + inteiro + ',' + partes[1];
}

// Formata percentual (ex: 12.5 -> 12,5%)
export function formatarPercentual(valor) {
    return String(valor).replace('.', ',') + '%';
}

// Valida os campos
export function validarDados(idadeTexto, rendaTexto, temCLT, temDividas) {
    const idadeLimpa = idadeTexto.trim();
    const rendaLimpa = rendaTexto.trim().replace(',', '.');

    if (idadeLimpa === '') {
        return 'Informe a idade do correntista.';
    }
    if (!/^\d+$/.test(idadeLimpa) || Number(idadeLimpa) <= 0) {
        return 'A idade deve ser um número inteiro maior que zero.';
    }
    if (rendaLimpa === '') {
        return 'Informe a renda anual do correntista.';
    }
    if (isNaN(Number(rendaLimpa)) || Number(rendaLimpa) <= 0) {
        return 'A renda anual deve ser um número maior que zero.';
    }
    if (temCLT === null) {
        return 'Selecione se o correntista possui vínculo CLT.';
    }
    if (temDividas === null) {
        return 'Selecione se o correntista possui dívidas.';
    }
    return '';
}

// Define o percentual pela idade
export function definirPercentualPorIdade(idade) {
    if (idade <= 24) {
        return 25;
    } else if (idade <= 49) {
        return 40;
    } else if (idade <= 64) {
        return 30;
    } else {
        return 15;
    }
}

// Analisa o crédito e devolve o resultado
export function calcularCredito(idade, renda, temCLT, temDividas) {
    // Regra 1: dívidas
    if (temDividas) {
        return {
            aprovado: false,
            motivo: 'O cliente possui dívidas.',
            valorCredito: 0,
        };
    }

    // Regra 2: percentual inicial
    const percentualInicial = definirPercentualPorIdade(idade);

    // Regra 3: vínculo CLT
    let percentualFinal = percentualInicial;
    if (!temCLT) {
        percentualFinal = percentualInicial / 2;
    }

    // Regra 4: valor máximo
    const valorCredito = (renda * percentualFinal) / 100;

    return {
        aprovado: true,
        percentualInicial: percentualInicial,
        percentualFinal: percentualFinal,
        valorCredito: valorCredito,
    };
}