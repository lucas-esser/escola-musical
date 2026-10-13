# Crédito Caixa Mobile

Aplicativo Expo para apoiar a análise de elegibilidade e estimativa do limite de crédito com base na idade, renda anual, vínculo CLT e existência de dívidas.

## Executar

1. Instale o Node.js LTS.
2. Instale as dependências com `npm install`.
3. Inicie o Expo com `npx expo start`.
4. Abra o QR code no Expo Go ou use um emulador Android/iOS.

Para gerar uma compilação Android local, configure também o JDK 17 e o Android Studio com SDK, NDK e CMake conforme o material do curso.

## Regras aplicadas

- Clientes com dívidas não têm direito a crédito.
- Até 24 anos: 25% da renda anual.
- De 25 a 49 anos: 40% da renda anual.
- De 50 a 64 anos: 30% da renda anual.
- A partir de 65 anos: 15% da renda anual.
- Sem vínculo CLT, o percentual é reduzido pela metade.

O resultado é uma estimativa baseada apenas nessas regras e não substitui a análise final da instituição.
