import Cabecalho from "@/components/Cabecalho";
import CampoTexto from "@/components/CampoTexto";
import OpcaoSimNao from "@/components/OpcaoSimNao";
import ResultadoCredito from "@/components/ResultadoCredito";
import { calcularCredito, validarDados } from "@/utils/analiseCredito";
import { StatusBar } from "expo-status-bar";
import { useState } from "react";
import {
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";

export default function Index() {
  // Estados dos campos
  const [idade, setIdade] = useState('');
  const [rendaAnual, setRendaAnual] = useState('');
  const [temCLT, setTemCLT] = useState(null);
  const [temDividas, setTemDividas] = useState(null);

  // Estados do resultado
  const [resultado, setResultado] = useState(null);
  const [erro, setErro] = useState('');

  // Analisa o crédito
  const analisarCredito = () => {
    const mensagemErro = validarDados(idade, rendaAnual, temCLT, temDividas);

    if (mensagemErro !== '') {
      setErro(mensagemErro);
      setResultado(null);
      return;
    }

    const idadeNumero = Number(idade.trim());
    const rendaNumero = Number(rendaAnual.trim().replace(',', '.'));

    const analise = calcularCredito(idadeNumero, rendaNumero, temCLT, temDividas);

    setErro('');
    setResultado({
      ...analise,
      idade: idadeNumero,
      renda: rendaNumero,
      temCLT: temCLT,
      temDividas: temDividas,
    });
  }

  // Limpa o formulário
  const limparCampos = () => {
    setIdade('');
    setRendaAnual('');
    setTemCLT(null);
    setTemDividas(null);
    setResultado(null);
    setErro('');
  }

  return (
    <KeyboardAvoidingView
      style={styles.tela}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <StatusBar style="light" />

      <ScrollView
        contentContainerStyle={styles.conteudo}
        keyboardShouldPersistTaps="handled"
      >
        <Cabecalho />

        {/* Formulário */}
        <View style={styles.card}>
          <CampoTexto
            rotulo="Idade"
            valor={idade}
            aoMudar={setIdade}
            exemplo="Ex: 35"
            tamanhoMaximo={3}
          />

          <CampoTexto
            rotulo="Renda anual (R$)"
            valor={rendaAnual}
            aoMudar={setRendaAnual}
            exemplo="Ex: 80000"
          />

          <OpcaoSimNao
            rotulo="Possui vínculo CLT?"
            valor={temCLT}
            aoSelecionar={setTemCLT}
          />

          <OpcaoSimNao
            rotulo="Possui dívidas?"
            valor={temDividas}
            aoSelecionar={setTemDividas}
          />

          {erro !== '' && (
            <View style={styles.caixa_erro}>
              <Text style={styles.texto_erro}>{erro}</Text>
            </View>
          )}

          <TouchableOpacity
            style={styles.botao_analisar}
            onPress={analisarCredito}
            activeOpacity={0.8}
          >
            <Text style={styles.botao_analisar_texto}>ANALISAR CRÉDITO</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.botao_limpar}
            onPress={limparCampos}
            activeOpacity={0.8}
          >
            <Text style={styles.botao_limpar_texto}>LIMPAR CAMPOS</Text>
          </TouchableOpacity>
        </View>

        <ResultadoCredito resultado={resultado} />
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  tela: {
    flex: 1,
    backgroundColor: '#EEF3F9',
  },
  conteudo: {
    paddingBottom: 40,
  },
  card: {
    backgroundColor: '#FFFFFF',
    marginHorizontal: 16,
    marginTop: 20,
    padding: 18,
    borderRadius: 16,
    elevation: 3,
  },
  caixa_erro: {
    backgroundColor: '#FDECEC',
    borderLeftWidth: 4,
    borderLeftColor: '#C62828',
    padding: 12,
    borderRadius: 8,
    marginTop: 16,
  },
  texto_erro: {
    color: '#C62828',
    fontSize: 14,
    fontWeight: '600',
  },
  botao_analisar: {
    backgroundColor: '#0066B3',
    height: 52,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 22,
  },
  botao_analisar_texto: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
  botao_limpar: {
    height: 52,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#0B2F5B',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 12,
  },
  botao_limpar_texto: {
    color: '#0B2F5B',
    fontSize: 16,
    fontWeight: 'bold',
  },
});