import { StyleSheet, Text, View } from "react-native";
import { formatarMoeda, formatarPercentual } from "@/utils/analiseCredito";

const ResultadoCredito = (props) => {
    const resultado = props.resultado;

    // Antes da primeira análise
    if (resultado === null) {
        return (
            <View style={styles_local.card_info}>
                <Text style={styles_local.info_texto}>
                    Preencha os dados do correntista e toque em ANALISAR CRÉDITO.
                </Text>
            </View>
        );
    }

    // Crédito aprovado
    if (resultado.aprovado) {
        return (
            <View style={[styles_local.card, styles_local.card_aprovado]}>
                <Text style={[styles_local.status, styles_local.texto_aprovado]}>
                    CRÉDITO APROVADO
                </Text>

                <Text style={styles_local.linha}>Idade: {resultado.idade} anos</Text>
                <Text style={styles_local.linha}>
                    Renda anual: {formatarMoeda(resultado.renda)}
                </Text>
                <Text style={styles_local.linha}>
                    Vínculo CLT: {resultado.temCLT ? 'Sim' : 'Não'}
                </Text>
                <Text style={styles_local.linha}>
                    Dívidas: {resultado.temDividas ? 'Sim' : 'Não'}
                </Text>
                <Text style={styles_local.linha}>
                    Percentual aplicado: {formatarPercentual(resultado.percentualFinal)}
                </Text>

                <Text style={styles_local.rotulo_valor}>Valor máximo do crédito</Text>
                <Text style={[styles_local.valor_destaque, styles_local.texto_aprovado]}>
                    {formatarMoeda(resultado.valorCredito)}
                </Text>
            </View>
        );
    }

    // Crédito não aprovado
    return (
        <View style={[styles_local.card, styles_local.card_reprovado]}>
            <Text style={[styles_local.status, styles_local.texto_reprovado]}>
                CRÉDITO NÃO APROVADO
            </Text>
            <Text style={styles_local.linha}>Motivo: {resultado.motivo}</Text>

            <Text style={styles_local.rotulo_valor}>Valor do crédito</Text>
            <Text style={[styles_local.valor_destaque, styles_local.texto_reprovado]}>
                {formatarMoeda(resultado.valorCredito)}
            </Text>
        </View>
    );
}

export default ResultadoCredito;

const styles_local = StyleSheet.create({
    card_info: {
        backgroundColor: '#DCE8F7',
        marginHorizontal: 16,
        marginTop: 20,
        padding: 18,
        borderRadius: 16,
    },
    info_texto: {
        color: '#0B2F5B',
        fontSize: 15,
        textAlign: 'center',
    },
    card: {
        marginHorizontal: 16,
        marginTop: 20,
        padding: 20,
        borderRadius: 16,
        borderWidth: 2,
        backgroundColor: '#FFFFFF',
    },
    card_aprovado: {
        borderColor: '#2E7D32',
    },
    card_reprovado: {
        borderColor: '#C62828',
    },
    status: {
        fontSize: 20,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 14,
    },
    texto_aprovado: {
        color: '#2E7D32',
    },
    texto_reprovado: {
        color: '#C62828',
    },
    linha: {
        fontSize: 15,
        color: '#333333',
        marginBottom: 6,
    },
    rotulo_valor: {
        fontSize: 14,
        color: '#555555',
        textAlign: 'center',
        marginTop: 14,
    },
    valor_destaque: {
        fontSize: 34,
        fontWeight: 'bold',
        textAlign: 'center',
        marginTop: 4,
    },
});