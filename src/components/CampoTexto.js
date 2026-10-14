import { StyleSheet, Text, TextInput } from "react-native";

const CampoTexto = (props) => {
    return (
        <>
            <Text style={styles_local.rotulo}>{props.rotulo}</Text>
            <TextInput
                style={styles_local.campo}
                value={props.valor}
                onChangeText={props.aoMudar}
                placeholder={props.exemplo}
                placeholderTextColor="#8A94A6"
                keyboardType="numeric"
                maxLength={props.tamanhoMaximo}
            />
        </>
    );
}

export default CampoTexto;

const styles_local = StyleSheet.create({
    rotulo: {
        fontSize: 15,
        fontWeight: '600',
        color: '#0B2F5B',
        marginBottom: 6,
        marginTop: 14,
    },
    campo: {
        borderWidth: 1,
        borderColor: '#C5D2E3',
        borderRadius: 10,
        paddingHorizontal: 14,
        height: 50,
        fontSize: 16,
        color: '#1B1B1B',
        backgroundColor: '#F7FAFD',
    },
});