# FisicaLudica
Projeto final para avaliação da disciplina Programação Avançada para Dispositivos Moveis. O projeto consiste em desenvolver uma aplicação para dispositivos móveis, na plataforma Android, que utilize conceitos e tecnologias trabalhados durante o curso.

| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **Física Lúdica**
| :label: Tecnologias | Kotlin, Android Framework, Google firestore, KorGe game engine
| :rocket: URL         | https://github.com/guilhermegbraz/Fisica-Ludica
| :fire: Desafio     | Desenvolver uma aplicação, gameficada, que simule física em 2D, especificamente movimento de projétil, para ser usado nos estudos de fenomenos mecanicos

![tela inputs](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/c107739d-fef7-4eb9-985d-6809ba73e370#vitrinedev)


## Proposta
  O objetivo desse projeto é oferecer aos alunos uma experiência mais interativa e lúdica para
aprender conceitos de física, especialmente para aqueles que têm dificuldades em visualizar
fenômenos abstratos. Além disso, o aplicativo visa ajudar os estudantes a desenvolver habilidades
cognitivas e lógicas, melhorando sua compreensão e interesse em física.

  Ao invés de simular a situação de maneira livre, o sistema irá através de jogos, guiar o
usuário a situações de lançamento que são comuns em questões e a compreender as grandezas que
interferem, e como elas interferem, na trajetória do objeto além nas propriedades que ele possui em
diferentes pontos, como velocidade, aceleração, etc.

  No jogo, o jogador irá receber as coordenadas no plano cartesiano de um alvo, e então
deverá estimar os valores das condições iniciais do lançamento para que o objeto intercepte o alvo.
Com múltiplas fases onde velocidade inicial ou ângulo de lançamento são fixados para que o
jogador encontre o valor da variável, variando também as condições em que o objeto deve atingir o
alvo.

## Como jogar
O apk do aplicativo esta disponivel em: https://drive.google.com/drive/folders/1kEuze-OaRMlJ2pXM7-_4rlmP-xz4YO0N?usp=sharing

voce pode instalar em seu smartphone ou então clonar o projeto e rodar no seu emulador

## Fluxo da utilização do app
#### 1- home:
![tela home](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/9b6a3f49-9d6a-4ef8-b0c3-5dc7c0d533f9)

#### 2- listagem de mapas:
![tela de listagem](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/a530afe7-438f-4e5f-ab8e-64734fce5e5d)

#### 3- Input de dados:
![tela inputs](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/557beeec-fa6e-426b-ba7e-9f1c6069a7d6)

#### 4- animação:
![lançamento](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/56f60ea4-69f1-4b91-9a7e-b8cea260a86c)

#### 5- resultados:
![resultado positivo](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/5a5613e8-8a57-4b98-a291-c2cf0abc6e68)
![resultado negativo](https://github.com/guilhermegbraz/Fisica-Ludica/assets/84741834/bb9992ce-4bfa-42dd-ae3a-58a78dbae51c)


## Detalhamento do sistema

Descrevendo de maneira simplificada, o aplicativo possui uma tela de seleção de
fase, uma para interação com o usuário onde ele insere os dados para a construção da
cena, então é exibido a animação de um canhão disparando uma maçã em direção ao
alvo de madeira, por fim é exibido ao usuário uma tela com seu resultado. O fluxograma
da aplicação pode ser visto na Figura 1

Dito isso, o aplicativo segue os seguintes padrões de arquitetura:
● SAMF(Single Activity Multiple Fragments): temos apenas uma activity, como
host, 6 fragmentos e um Dialog que é utilizado como pause com exibição de
dicas para o usuário

● MVVM(Model View ViewModel): a aplicação possui uma ViewModel que
acessa e modifica as entidades da camada Model e utiliza de componentes
lifecicle-aware para comunicação com a View.

### Animação

A animação do projetil sendo lançado foi desenvolvido usando KorGe, uma engine de jogos em kotlin. Analisando exemplos de códigos utilizando KorGe, foi possível
verificar que essa engine permite fazer tarefas não tão triviais com poucas linhas de
códigos e de maneira intuitiva, sua api simplifica tarefas como criar um corpo com
forma e textura em um mundo box2D e desenhar um objeto em tela.

Então, por esses motivos, KorGe foi a engine escolhida para o desenvolvimento
da animação. Posteriormente decidi por apenas lançar a cena em um fragmento para
facilitar a utilização da API do Android Framework, ao invés de uma aplicação
inteiramente controlada pela engine.

### Dados

Foi necessário tomar a decisão de como modelar a base de dados para
atender aos requisitos iniciais, os requisitos eram: Salve o palpite que o usuário faz nas
fases para que se ele retornar a uma mesma fase, seu último palpite estar lá; As fases só
aparecem disponíveis para o usuário caso ele tenha acertado o desafio da fase anterior,
com exceção da fase 1 que está disponível desde que o usuário realiza login no jogo.


Dito isso, a solução utilizada foi criar uma coleção onde cada documento é uma
fase do jogo, i.e um GameLevel, porém como é necessário que para cada fase tenhamos
a informação do último palpite que o usuário inseriu e também se essa fase está
disponível para o usuário, foi decidido criar subcoleções dentro de cada documento.

Na primeira vez que o jogador abrir uma fase do jogo, os valores iniciais serão
setados com o valor salvo no documento gameLevel, porém a partir do primeiro palpite
inserido a aplicação salva esse palpite em uma subcoleção denominada answers, e essa
subcoleção é pertencente apenas à fase específica. 

O documento da coleção answer guarda velocidade e ângulo inseridos e também o id do usuário.
Essa subcoleção é acessada pelo sistema sempre que a aplicação construir a tela
para interação com o jogador, para recuperar o último palpite, e também quando o
jogador apertar o botão de lançar a cena, antes de lançar a cena animada a aplicação
salva, ou atualiza, o palpite do usuário no documento.


A mesma solução foi utilizada para a manutenção de fases que estão disponíveis
para o usuário, há uma coleção denominada usersEnable dentro dos documentos das
fases, e nela está sendo guardado o id de quais usuário possuem aquela fase liberada.
Nesse documento é salvo o id do usuário, recuperamos essa informação para construir a
o fragmento de listagem, ademais também acessamos essa coleção sempre que o usuário
conclui uma fase do jogo para então a próxima ser liberada. Com exceção da primeira

fase que é liberada quando o usuário faz login pela primeira vez na aplicação.

## Demonstração da aplicação

https://youtu.be/h8krzIcr_fA
