import { StyleSheet, Text, TouchableOpacity, View } from "react-native";

const OpcaoSimNao = (props) => {
    // Botão Sim ou Não
    const botao = (texto, valor) => {
        const selecionado = props.valor === valor;
        return (
            <TouchableOpacity
                style={[styles_local.opcao, selecionado && styles_local.opcao_selecionada]}
                onPress={() => props.aoSelecionar(valor)}
                activeOpacity={0.8}
            >
                <Text style={[styles_local.texto, selecionado && styles_local.texto_selecionado]}>
                    {texto}
                </Text>
            </TouchableOpacity>
        );
    }

    return (
        <>
            <Text style={styles_local.rotulo}>{props.rotulo}</Text>
            <View style={styles_local.linha}>
                {botao('Sim', true)}
                {botao('Não', false)}
            </View>
        </>
    );
}

export default OpcaoSimNao;

const styles_local = StyleSheet.create({
    rotulo: {
        fontSize: 15,
        fontWeight: '600',
        color: '#0B2F5B',
        marginBottom: 6,
        marginTop: 14,
    },
    linha: {
        flexDirection: 'row',
    },
    opcao: {
        flex: 1,
        height: 48,
        borderWidth: 1,
        borderColor: '#0066B3',
        borderRadius: 10,
        alignItems: 'center',
        justifyContent: 'center',
        marginRight: 10,
        backgroundColor: '#FFFFFF',
    },
    opcao_selecionada: {
        backgroundColor: '#0066B3',
    },
    texto: {
        fontSize: 16,
        fontWeight: '600',
        color: '#0066B3',
    },
    texto_selecionado: {
        color: '#FFFFFF',
    },
});