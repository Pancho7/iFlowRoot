

Jo�o coloquei no SVN em CMAbrantes\Webservices o c�digo dos blocos.

 
Para configurares deves alterar o ficheiro webservices.properties.

 

Existem 3 blocos de Cria��o de registo e um de leitura de registo:

 

BlockCreateRegistoAssiduidade: usa o cliente gerado pelo eclipse e funciona com o WS dummy (� o primeiro bloco que foi testado)

JAVA 6: BlockCreateRegistoAssiduidade1: usa o c�digo retirado de http://blogs.msdn.com/b/freddyk/archive/2010/01/19/connecting-to-nav-web-services-from-java.aspx.

BlockCreateRegistoAssiduidade2: usa o c�digo retirado de http://hc.apache.org/httpcomponents-client-ga/tutorial/html/authentication.html. Usa o NTLM e � o que tem mais probabilidade de funcionar.

                                                               (para usar este tens que alterar o pom.xml do iflow-web)

 

Para cada um destes existe uma biblioteca para o editor (cidadela.xml, cidadela1.xml, cidadela2.xml) e um fluxo de teste (CriarAssiduidade.xml, CriarAssiduidade1.xml, CriarAssiduidade2.xml)

 

O CriarAssiduidade.xml � o mesmo que j� te enviei antes e n�o precisas de reinstalar.

 