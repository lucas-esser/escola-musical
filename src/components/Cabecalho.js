import { StyleSheet, Text, View } from "react-native";

const Cabecalho = () => {
    return (
        <View style={styles_local.cabecalho}>
            <Text style={styles_local.icone}>🏦</Text>
            <Text style={styles_local.titulo}>Análise de Crédito</Text>
            <Text style={styles_local.subtitulo}>Simulador de crédito para correntistas</Text>
        </View>
    );
}

export default Cabecalho;

const styles_local = StyleSheet.create({
    cabecalho: {
        backgroundColor: '#0B2F5B',
        paddingTop: 50,
        paddingBottom: 30,
        paddingHorizontal: 20,
        alignItems: 'center',
        borderBottomLeftRadius: 24,
        borderBottomRightRadius: 24,
    },
    icone: {
        fontSize: 40,
        marginBottom: 6,
    },
    titulo: {
        color: '#FFFFFF',
        fontSize: 26,
        fontWeight: 'bold',
    },
    subtitulo: {
        color: '#BBD3F0',
        fontSize: 14,
        marginTop: 4,
    },
});