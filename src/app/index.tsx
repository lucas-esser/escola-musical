import { useState } from "react";
import {
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

type Decision = {
  approved: boolean;
  rate: number;
  amount: number;
};

const palette = {
  navy: "#092A3B",
  blue: "#0878B9",
  teal: "#00A6A0",
  ink: "#183341",
  muted: "#71848E",
  line: "#DCE6E9",
  paper: "#F3F7F8",
  white: "#FFFFFF",
  green: "#137A58",
  greenBg: "#E7F5EF",
  red: "#AA4141",
  redBg: "#FCECEC",
};

function parseIncome(value: string) {
  const normalized = value.trim().replace(/R\$\s?/gi, "").replace(/\s/g, "");
  if (!normalized) return 0;
  const parsed = normalized.includes(",")
    ? Number(normalized.replace(/\./g, "").replace(",", "."))
    : Number(normalized);
  return Number.isFinite(parsed) ? parsed : 0;
}

function money(value: number) {
  return value.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
    minimumFractionDigits: 2,
  });
}

function rateForAge(age: number) {
  if (age <= 24) return 0.25;
  if (age <= 49) return 0.4;
  if (age <= 64) return 0.3;
  return 0.15;
}

function Choice({
  selected,
  label,
  onPress,
}: {
  selected: boolean;
  label: string;
  onPress: () => void;
}) {
  return (
    <Pressable
      accessibilityRole="radio"
      accessibilityState={{ selected }}
      onPress={onPress}
      style={[styles.choice, selected && styles.choiceSelected]}
    >
      <Text style={[styles.choiceText, selected && styles.choiceTextSelected]}>{label}</Text>
    </Pressable>
  );
}

export default function Index() {
  const [name, setName] = useState("");
  const [age, setAge] = useState("");
  const [income, setIncome] = useState("");
  const [clt, setClt] = useState<boolean | null>(null);
  const [debt, setDebt] = useState<boolean | null>(null);
  const [decision, setDecision] = useState<Decision | null>(null);
  const [error, setError] = useState("");

  function analyze() {
    const parsedAge = Number(age);
    const parsedIncome = parseIncome(income);
    if (!age || !Number.isInteger(parsedAge) || parsedAge < 0 || parsedAge > 120) {
      setError("Informe uma idade válida, entre 0 e 120 anos.");
      setDecision(null);
      return;
    }
    if (parsedIncome <= 0) {
      setError("Informe uma renda anual maior que zero.");
      setDecision(null);
      return;
    }
    if (clt === null || debt === null) {
      setError("Responda às perguntas sobre vínculo CLT e dívidas.");
      setDecision(null);
      return;
    }
    setError("");
    const rate = clt ? rateForAge(parsedAge) : rateForAge(parsedAge) / 2;
    setDecision({
      approved: !debt,
      rate,
      amount: !debt ? parsedIncome * rate : 0,
    });
  }

  function resetForm() {
    setName("");
    setAge("");
    setIncome("");
    setClt(null);
    setDebt(null);
    setDecision(null);
    setError("");
  }

  function fillExample(example: "joao" | "ana" | "vorcaro") {
    if (example === "joao") {
      setName("João");
      setAge("23");
      setIncome("20000");
      setClt(true);
      setDebt(false);
    } else if (example === "ana") {
      setName("Ana");
      setAge("35");
      setIncome("80000");
      setClt(false);
      setDebt(false);
    } else {
      setName("Vorcaro");
      setAge("43");
      setIncome("15000000");
      setClt(false);
      setDebt(true);
    }
    setDecision(null);
    setError("");
  }

  return (
    <SafeAreaView style={styles.safeArea} edges={["top", "bottom"]}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === "ios" ? "padding" : undefined}
      >
        <ScrollView
          contentContainerStyle={styles.scrollContent}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <View style={styles.header}>
            <View style={styles.headerTop}>
              <View style={styles.brandMark}>
                <Text style={styles.brandMarkText}>C</Text>
              </View>
              <Text style={styles.brandName}>CRÉDITO CAIXA</Text>
              <View style={styles.internalTag}><Text style={styles.internalText}>USO INTERNO</Text></View>
            </View>
            <Text style={styles.eyebrow}>ANÁLISE DE CRÉDITO</Text>
            <Text style={styles.title}>Vamos analisar{ "\n" }o perfil do cliente</Text>
            <Text style={styles.subtitle}>Preencha os dados para consultar o limite estimado.</Text>
            <View style={styles.headerAccent} />
          </View>

          <View style={styles.content}>
            <View style={styles.sectionHeading}>
              <View style={styles.stepBadge}><Text style={styles.stepNumber}>01</Text></View>
              <View>
                <Text style={styles.sectionTitle}>Dados do correntista</Text>
                <Text style={styles.sectionCaption}>As informações são usadas nesta análise.</Text>
              </View>
            </View>

            <View style={styles.field}>
              <Text style={styles.label}>Nome do cliente <Text style={styles.optional}>(opcional)</Text></Text>
              <TextInput
                accessibilityLabel="Nome do cliente"
                autoCapitalize="words"
                onChangeText={setName}
                placeholder="Ex.: Maria Silva"
                placeholderTextColor="#9AAAB0"
                returnKeyType="next"
                style={styles.input}
                value={name}
              />
            </View>

            <View style={styles.row}>
              <View style={[styles.field, styles.ageField]}>
                <Text style={styles.label}>Idade</Text>
                <View style={styles.inputWrap}>
                  <TextInput
                    accessibilityLabel="Idade"
                    keyboardType="number-pad"
                    maxLength={3}
                    onChangeText={setAge}
                    placeholder="Ex.: 35"
                    placeholderTextColor="#9AAAB0"
                    style={styles.inputInner}
                    value={age}
                  />
                  <Text style={styles.suffix}>anos</Text>
                </View>
              </View>
              <View style={[styles.field, styles.incomeField]}>
                <Text style={styles.label}>Renda anual</Text>
                <View style={styles.inputWrap}>
                  <Text style={styles.prefix}>R$</Text>
                  <TextInput
                    accessibilityLabel="Renda anual em reais"
                    keyboardType="decimal-pad"
                    onChangeText={setIncome}
                    placeholder="0,00"
                    placeholderTextColor="#9AAAB0"
                    style={styles.inputInner}
                    value={income}
                  />
                </View>
              </View>
            </View>

            <View style={styles.field}>
              <Text style={styles.label}>Possui vínculo empregatício CLT?</Text>
              <View accessibilityRole="radiogroup" style={styles.choiceRow}>
                <Choice label="Sim" selected={clt === true} onPress={() => setClt(true)} />
                <Choice label="Não" selected={clt === false} onPress={() => setClt(false)} />
              </View>
            </View>

            <View style={styles.field}>
              <Text style={styles.label}>Possui dívidas?</Text>
              <View accessibilityRole="radiogroup" style={styles.choiceRow}>
                <Choice label="Sim" selected={debt === true} onPress={() => setDebt(true)} />
                <Choice label="Não" selected={debt === false} onPress={() => setDebt(false)} />
              </View>
            </View>

            {error ? <Text accessibilityRole="alert" style={styles.error}>{error}</Text> : null}

            <Pressable accessibilityRole="button" onPress={analyze} style={styles.primaryButton}>
              <Text style={styles.primaryButtonText}>Consultar elegibilidade</Text>
              <Text style={styles.buttonArrow}>→</Text>
            </Pressable>

            {decision ? (
              <View style={[styles.resultCard, decision.approved ? styles.resultApproved : styles.resultDenied]}>
                <View style={styles.resultTop}>
                  <View style={[styles.resultIcon, decision.approved ? styles.iconApproved : styles.iconDenied]}>
                    <Text style={[styles.resultIconText, decision.approved ? styles.iconTextApproved : styles.iconTextDenied]}>
                      {decision.approved ? "✓" : "!"}
                    </Text>
                  </View>
                  <View style={styles.resultTitleWrap}>
                    <Text style={[styles.resultStatus, decision.approved ? styles.approvedText : styles.deniedText]}>
                      {decision.approved ? "CRÉDITO APROVADO" : "SEM DIREITO A CRÉDITO"}
                    </Text>
                    <Text style={styles.resultName}>{name.trim() ? `Análise de ${name.trim()}` : "Resultado da análise"}</Text>
                  </View>
                </View>
                {decision.approved ? (
                  <>
                    <Text style={styles.amountLabel}>LIMITE MÁXIMO ESTIMADO</Text>
                    <Text accessibilityLiveRegion="polite" style={styles.amount}>{money(decision.amount)}</Text>
                    <View style={styles.calculation}>
                      <Text style={styles.calculationText}>
                        {money(parseIncome(income))} × {Math.round(decision.rate * 100)}% = {money(decision.amount)}
                      </Text>
                    </View>
                  </>
                ) : (
                  <Text style={styles.denialNote}>A existência de dívidas impede a concessão de crédito conforme os critérios informados.</Text>
                )}
                <Pressable onPress={resetForm} style={styles.newAnalysisButton}>
                  <Text style={styles.newAnalysisText}>Nova análise</Text>
                </Pressable>
              </View>
            ) : null}

            <View style={styles.rulesCard}>
              <View style={styles.rulesHeader}>
                <Text style={styles.rulesTitle}>Critérios de referência</Text>
                <Text style={styles.infoMark}>i</Text>
              </View>
              <View style={styles.ruleRow}><Text style={styles.ruleAge}>Até 24 anos</Text><Text style={styles.ruleRate}>25% da renda</Text></View>
              <View style={styles.ruleRow}><Text style={styles.ruleAge}>25 a 49 anos</Text><Text style={styles.ruleRate}>40% da renda</Text></View>
              <View style={styles.ruleRow}><Text style={styles.ruleAge}>50 a 64 anos</Text><Text style={styles.ruleRate}>30% da renda</Text></View>
              <View style={[styles.ruleRow, styles.lastRule]}><Text style={styles.ruleAge}>65 anos ou mais</Text><Text style={styles.ruleRate}>15% da renda</Text></View>
              <Text style={styles.rulesFootnote}>Sem vínculo CLT, o percentual é reduzido pela metade. Clientes com dívidas não são elegíveis.</Text>
            </View>

            <View style={styles.examples}>
              <Text style={styles.examplesTitle}>TESTAR COM EXEMPLO</Text>
              <View style={styles.exampleRow}>
                <Pressable onPress={() => fillExample("joao")} style={styles.exampleChip}><Text style={styles.exampleText}>João</Text></Pressable>
                <Pressable onPress={() => fillExample("ana")} style={styles.exampleChip}><Text style={styles.exampleText}>Ana</Text></Pressable>
                <Pressable onPress={() => fillExample("vorcaro")} style={styles.exampleChip}><Text style={styles.exampleText}>Vorcaro</Text></Pressable>
              </View>
            </View>

            <Text style={styles.disclaimer}>Estimativa baseada nos critérios informados. A concessão final depende da análise de crédito da instituição.</Text>
            <Text style={styles.footer}>CAIXA ECONÔMICA FEDERAL · FERRAMENTA DE APOIO</Text>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  safeArea: { flex: 1, backgroundColor: palette.navy },
  scrollContent: { flexGrow: 1, backgroundColor: palette.paper },
  header: { backgroundColor: palette.navy, paddingHorizontal: 24, paddingTop: 12, paddingBottom: 28, overflow: "hidden" },
  headerTop: { flexDirection: "row", alignItems: "center", marginBottom: 30 },
  brandMark: { width: 32, height: 32, borderRadius: 10, alignItems: "center", justifyContent: "center", backgroundColor: palette.teal, marginRight: 9 },
  brandMarkText: { color: palette.white, fontSize: 19, fontWeight: "800" },
  brandName: { color: palette.white, fontSize: 12, fontWeight: "800", letterSpacing: 1.4, flex: 1 },
  internalTag: { paddingVertical: 6, paddingHorizontal: 9, borderRadius: 20, backgroundColor: "#173F50" },
  internalText: { fontSize: 9, fontWeight: "700", color: "#9DCBD5", letterSpacing: 0.8 },
  eyebrow: { color: "#5FD2C5", fontSize: 10, fontWeight: "800", letterSpacing: 2, marginBottom: 11 },
  title: { color: palette.white, fontSize: 30, lineHeight: 36, fontWeight: "700", letterSpacing: -0.7 },
  subtitle: { color: "#AAC0C8", fontSize: 14, lineHeight: 21, marginTop: 10 },
  headerAccent: { position: "absolute", width: 170, height: 170, borderWidth: 1, borderColor: "#204958", borderRadius: 85, right: -72, bottom: -104 },
  content: { paddingHorizontal: 20, paddingTop: 24, paddingBottom: 30 },
  sectionHeading: { flexDirection: "row", alignItems: "center", marginBottom: 22 },
  stepBadge: { width: 38, height: 38, borderRadius: 12, backgroundColor: "#DDF1EF", alignItems: "center", justifyContent: "center", marginRight: 12 },
  stepNumber: { color: "#007C7A", fontWeight: "800", fontSize: 12 },
  sectionTitle: { color: palette.ink, fontWeight: "700", fontSize: 17 },
  sectionCaption: { color: palette.muted, fontSize: 12, marginTop: 3 },
  field: { marginBottom: 18 },
  label: { color: palette.ink, fontSize: 13, fontWeight: "600", marginBottom: 8 },
  optional: { color: palette.muted, fontWeight: "400", fontSize: 12 },
  input: { height: 50, borderWidth: 1, borderColor: palette.line, borderRadius: 12, backgroundColor: palette.white, paddingHorizontal: 14, color: palette.ink, fontSize: 15 },
  row: { flexDirection: "row", gap: 12 },
  ageField: { flex: 0.8 },
  incomeField: { flex: 1.4 },
  inputWrap: { height: 50, borderWidth: 1, borderColor: palette.line, borderRadius: 12, backgroundColor: palette.white, flexDirection: "row", alignItems: "center", paddingHorizontal: 12 },
  inputInner: { flex: 1, color: palette.ink, fontSize: 15, paddingVertical: 0, minWidth: 0 },
  suffix: { color: palette.muted, fontSize: 12 },
  prefix: { color: palette.muted, fontSize: 13, marginRight: 6, fontWeight: "600" },
  choiceRow: { flexDirection: "row", gap: 10 },
  choice: { minHeight: 46, flex: 1, borderRadius: 12, borderWidth: 1, borderColor: palette.line, backgroundColor: palette.white, alignItems: "center", justifyContent: "center" },
  choiceSelected: { borderColor: palette.blue, backgroundColor: "#EAF5FA" },
  choiceText: { color: palette.muted, fontSize: 14, fontWeight: "600" },
  choiceTextSelected: { color: palette.blue, fontWeight: "700" },
  error: { color: palette.red, fontSize: 12, lineHeight: 18, marginTop: -4, marginBottom: 12 },
  primaryButton: { minHeight: 54, borderRadius: 14, backgroundColor: palette.blue, alignItems: "center", justifyContent: "center", flexDirection: "row", marginTop: 2, shadowColor: "#005A88", shadowOpacity: 0.18, shadowRadius: 10, shadowOffset: { width: 0, height: 5 }, elevation: 3 },
  primaryButtonText: { color: palette.white, fontSize: 15, fontWeight: "700" },
  buttonArrow: { color: palette.white, fontSize: 20, marginLeft: 9, marginTop: -2 },
  resultCard: { marginTop: 18, padding: 18, borderRadius: 16, borderWidth: 1 },
  resultApproved: { backgroundColor: palette.greenBg, borderColor: "#BCE4D2" },
  resultDenied: { backgroundColor: palette.redBg, borderColor: "#F0C9C9" },
  resultTop: { flexDirection: "row", alignItems: "center", marginBottom: 18 },
  resultIcon: { width: 36, height: 36, borderRadius: 18, alignItems: "center", justifyContent: "center", marginRight: 11 },
  iconApproved: { backgroundColor: "#C7EBDC" },
  iconDenied: { backgroundColor: "#F4D8D8" },
  resultIconText: { fontSize: 19, fontWeight: "800" },
  iconTextApproved: { color: palette.green },
  iconTextDenied: { color: palette.red },
  resultTitleWrap: { flex: 1 },
  resultStatus: { fontSize: 12, fontWeight: "800", letterSpacing: 0.7 },
  approvedText: { color: palette.green },
  deniedText: { color: palette.red },
  resultName: { color: palette.muted, fontSize: 12, marginTop: 3 },
  amountLabel: { color: "#5A7870", fontSize: 9, fontWeight: "800", letterSpacing: 1.2 },
  amount: { color: "#105D46", fontSize: 29, fontWeight: "800", letterSpacing: -0.6, marginTop: 5 },
  calculation: { borderTopWidth: 1, borderTopColor: "#CDE8DC", marginTop: 13, paddingTop: 11 },
  calculationText: { color: "#477467", fontSize: 12, fontWeight: "600" },
  denialNote: { color: "#805454", fontSize: 13, lineHeight: 19, marginTop: -5 },
  newAnalysisButton: { alignSelf: "flex-start", marginTop: 14, paddingVertical: 8 },
  newAnalysisText: { color: palette.blue, fontSize: 13, fontWeight: "700" },
  rulesCard: { marginTop: 22, padding: 16, backgroundColor: palette.white, borderRadius: 15, borderWidth: 1, borderColor: "#E7EDEF" },
  rulesHeader: { flexDirection: "row", alignItems: "center", justifyContent: "space-between", marginBottom: 8 },
  rulesTitle: { color: palette.ink, fontSize: 13, fontWeight: "700" },
  infoMark: { overflow: "hidden", width: 18, height: 18, borderRadius: 9, textAlign: "center", textAlignVertical: "center", color: palette.blue, backgroundColor: "#E8F3F8", fontSize: 12, fontWeight: "700" },
  ruleRow: { flexDirection: "row", alignItems: "center", justifyContent: "space-between", minHeight: 35, borderBottomWidth: 1, borderBottomColor: "#EDF1F2" },
  lastRule: { borderBottomWidth: 0 },
  ruleAge: { color: "#566D77", fontSize: 12 },
  ruleRate: { color: palette.ink, fontSize: 12, fontWeight: "700" },
  rulesFootnote: { color: palette.muted, fontSize: 11, lineHeight: 16, marginTop: 8 },
  examples: { marginTop: 21 },
  examplesTitle: { color: palette.muted, fontSize: 9, fontWeight: "800", letterSpacing: 1.4, marginBottom: 10 },
  exampleRow: { flexDirection: "row", gap: 8 },
  exampleChip: { backgroundColor: "#E7EEF0", paddingVertical: 8, paddingHorizontal: 13, borderRadius: 20 },
  exampleText: { color: "#48616C", fontSize: 11, fontWeight: "600" },
  disclaimer: { color: "#82929A", fontSize: 10, lineHeight: 15, marginTop: 21 },
  footer: { color: "#9AA9AF", fontSize: 8, fontWeight: "700", letterSpacing: 0.8, textAlign: "center", marginTop: 17 },
});
