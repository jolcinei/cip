
import com.jaks.cip.enuns.EnumCodigoErro;
import com.jaks.cip.enuns.EnumInstituidorArranjoPagamento;
import com.jaks.cip.enuns.EnumRetornoRequisicao;
import com.jaks.cip.enuns.EnumServicosEventos;
import com.jaks.cip.enuns.EnumTipoConta;
import com.jaks.cip.enuns.EnumTipoRetornado;
import com.jaks.cip.model.Arquivo;
import com.jaks.cip.model.Centralizadora;
import com.jaks.cip.model.Credenciador;
import com.jaks.cip.model.PontoVenda;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jolcinei
 */
public class geradorArquivo extends JFrame {

    /**
     * @param args the command line arguments
     */
    static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    static final Date now = Calendar.getInstance().getTime();
    static final String data = simpleDateFormat.format(now);

    //Aplicativo executÃ¡vel.
    private JMenu jMenu1;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private Separator jSeparator1;
    private JTextArea textArea;
    private JScrollPane jScrollPane1;
    private JFileChooser fileChooser;
    private JPanel panel;

    public geradorArquivo() {
        iniciarComponentes();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Aplicação">
    private void iniciarComponentes() {
        //setSize(800, 600);
        setLocationRelativeTo(null);
        panel = new JPanel();
        textArea = new JTextArea();
        jScrollPane1 = new JScrollPane(textArea);
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jSeparator1 = new Separator();
        fileChooser = new JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setBounds(10, 10, 800, 600);
        panel.setLayout(null);
        setContentPane(panel);
        textArea.setEditable(false);
        textArea.setColumns(10);
        textArea.setRows(30);
        jScrollPane1.setBounds(10, 10, 800, 600);
        panel.add(jScrollPane1);

        jMenu1.setText("File");

        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarArquivo(e);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator1);
        jMenuItem2.setText("Sair");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }// </editor-fold>

    public static void main(String[] args) throws Exception {
        //Aplicativo executÃ¡vel.
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new geradorArquivo().setVisible(true);
            }
        });

        //inicial sem executÃ¡vel.
        Credenciador credenciador = gerarDadosCredenciador();

        //Ate 50.000 registros da centralizadora por arquivo
        List<Centralizadora> centralizadoras = gerarDadosCentralizadoras();

        //Seta as credenciadoras vinculadas.
        credenciador.setCentralizadoras(centralizadoras);
        EnumServicosEventos tipo = EnumServicosEventos.ASLC031;
        //Auto incrementar a sequencia, sendo que nao pode haver a mesma sequencia de envio no mesmo dia.
        //Sequencia deve ter 5 posicoes.
        String sequencia = "00001";
        //Gera cabeÃƒÂ§alho do arquivo a ser enviado
        Arquivo arquivo = gerarDadosArquivo("ASCL" + tipo + "_" + credenciador.getCNPJBaseCreddr() + "_" + data + "_" + sequencia, tipo);
        arquivo.setCredenciador(credenciador);

        generateXMLFile(arquivo, tipo.getCodigo(), sequencia);
        Arquivo retornoXML = lerArquivoXML027("D:\\Projetos\\Webtik\\CIP\\ASLC027\\Sucesso\\ASLC027_11111111_20170522_00001_RET.xml");
        resultadoArquivoXML(retornoXML);
    }

    private void selecionarArquivo(java.awt.event.ActionEvent evt) {

        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // What to do with the file, e.g. display it in a TextArea
                Arquivo arq = null;
                String nomeArqu = file.getAbsolutePath();
                String[] partes = nomeArqu.split("_");
                String part = partes[0];

                if (part.contains("027")) {
                    arq = lerArquivoXML027(file.getAbsolutePath());
                    arq.setServicosEventos(EnumServicosEventos.ASLC027);
                } else if (part.contains("029")) {
                    arq = lerArquivoXML029(file.getAbsolutePath());
                    arq.setServicosEventos(EnumServicosEventos.ASLC029);
                } else if (part.contains("031")) {
                    arq = lerArquivoXML031(file.getAbsolutePath());
                    arq.setServicosEventos(EnumServicosEventos.ASLC031);
                } else if (part.contains("022")) {
                    arq = lerArquivoXML022(file.getAbsolutePath());
                    arq.setServicosEventos(EnumServicosEventos.ASLC022);
                }

                final String resultadoArquivoXML = resultadoArquivoXML(arq);
                textArea.setText(resultadoArquivoXML);
                //textArea.read(new FileReader(file.getAbsolutePath()), null);
                repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Problemas ao acessar o arquivo: " + file.getAbsolutePath());
            }
        } else {
            System.out.println("Cancelado pelo usuÃ¡rio.");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Ler arquivo ASLC027">
    public static Arquivo lerArquivoXML027(String absolutePath) {
        Arquivo arquivoret = new Arquivo();
        Credenciador cred = null;
        EnumTipoRetornado tipoRetornado = EnumTipoRetornado.ACTO;
        List<EnumCodigoErro> codigoErros = new ArrayList<>(EnumSet.allOf(EnumCodigoErro.class));
        List<Centralizadora> centralizadoras = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(absolutePath);

            NodeList cabecalho = doc.getElementsByTagName("BCARQ");

            int tamanhoCabecalho = cabecalho.getLength();

            for (int a = 0; a < tamanhoCabecalho; a++) {
                Node nodeCabecalho = cabecalho.item(a);
                if (nodeCabecalho.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList itensCabecalho = nodeCabecalho.getChildNodes();
                    int tamanhoFilhosCabecalho = itensCabecalho.getLength();
                    for (int b = 0; b < tamanhoFilhosCabecalho; b++) {
                        Node noCabFilho = itensCabecalho.item(b);

                        if (noCabFilho.getNodeType() == Node.ELEMENT_NODE) {
                            Element elCabFilho = (Element) noCabFilho;

                            switch (elCabFilho.getTagName()) {
                                case "NomArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNomArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setNomArq(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlEmis":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlEmis(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlEmis(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlDestOr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlDestOr(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlDestOr(elCabFilho.getTextContent());
                                    break;
                                case "ISPBEmissor":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBEmissor(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBEmissor(elCabFilho.getTextContent());
                                    break;
                                case "ISPBDestinatario":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBDestinatario(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBDestinatario(elCabFilho.getTextContent());
                                    break;
                                case "DtHrArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtHrArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtHrArq(elCabFilho.getTextContent());
                                    break;
                                case "DtRef":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtRef(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtRef(elCabFilho.getTextContent());
                                    break;
                            }
                        }
                    }
                }
            }
            NodeList nodeListLiq = doc.getElementsByTagName("Grupo_ASLC027RET_LiquidTranscCredActo");

            int tamanho;

            tamanho = nodeListLiq.getLength();
            if (tamanho == 0) {
                nodeListLiq = doc.getElementsByTagName("Grupo_ASLC027RET_LiquidTranscCredRecsdo");
                tipoRetornado = EnumTipoRetornado.RECSDO;
            }
            tamanho = nodeListLiq.getLength();

            for (int i = 0; i < tamanho; i++) {
                Node noCred = nodeListLiq.item(i);
                cred = new Credenciador();
                cred.setEnumTipoRetornado(tipoRetornado);
                if (noCred.getNodeType() == Node.ELEMENT_NODE) {
                    //Element elCred = (Element) noCred;
                    NodeList nodeListCr = noCred.getChildNodes();
                    int tamanhoFil = nodeListCr.getLength();
                    for (int j = 0; j < tamanhoFil; j++) {
                        Node fil = nodeListCr.item(j);
                        Centralizadora nCentralizadora = null;
                        Centralizadora nCentralizadoraRec = null;
                        if (fil.getNodeType() == Node.ELEMENT_NODE) {
                            Element elFilho = (Element) fil;

                            switch (elFilho.getTagName()) {
                                case "IdentdPartPrincipal":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentdPartPrincipal(codigoErro);
                                        }
                                    }
                                    cred.setIdentdPartPrincipal(elFilho.getTextContent());
                                    break;
                                case "IdentPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "IdentdPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "CNPJBaseCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJBaseCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJBaseCreddr(elFilho.getTextContent());
                                    break;
                                case "CNPJCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJCreddr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFDevdr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFDevdr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFDevdr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFCredr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFCredr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFCredr(elFilho.getTextContent());
                                    break;
                                case "AgCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroAgCreddr(codigoErro);
                                        }
                                    }
                                    cred.setAgCreddr(elFilho.getTextContent());
                                    break;
                                case "CtCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCtCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCtCreddr(elFilho.getTextContent());
                                    break;
                                case "NomeCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroNomeCreddr(codigoErro);
                                        }
                                    }
                                    cred.setNomeCreddr(elFilho.getTextContent());
                                    break;
                                case "SitRetReq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroSitRetReq(codigoErro);
                                        }
                                    }
                                    List<EnumRetornoRequisicao> retornoRequisicaos = new ArrayList<>(EnumSet.allOf(EnumRetornoRequisicao.class));
                                    for (EnumRetornoRequisicao retornoRequisicao : retornoRequisicaos) {
                                        if (elFilho.getTextContent().equals(retornoRequisicao.getCodigo())) {
                                            cred.setSitRetReq(retornoRequisicao);
                                        }
                                    }
                                    break;
                                case "Grupo_ASLC027RET_CentrlzActo":
                                    NodeList nodeListCentr = elFilho.getChildNodes();

                                    nCentralizadora = new Centralizadora();
                                    nCentralizadora.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                    List<PontoVenda> pontoVendas = new ArrayList<>();

                                    int tamanhoFilCent = nodeListCentr.getLength();

                                    for (int l = 0; l < tamanhoFilCent; l++) {

                                        Node filCent = nodeListCentr.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadora.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC027RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVenda)) {
                                                            pontoVendas.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                                case "Grupo_ASLC027RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = filCent.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVendaRec)) {
                                                            pontoVendas.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadora)) {
                                        centralizadoras.add(nCentralizadora);
                                        nCentralizadora = null;
                                    }
                                case "Grupo_ASLC027RET_CentrlzRecsdo":
                                    NodeList nodeListCentrRec = elFilho.getChildNodes();

                                    nCentralizadoraRec = new Centralizadora();
                                    nCentralizadoraRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                    List<PontoVenda> pontoVendasRec = new ArrayList<>();

                                    int tamanhoFilCentRec = nodeListCentrRec.getLength();

                                    for (int l = 0; l < tamanhoFilCentRec; l++) {

                                        Node filCent = nodeListCentrRec.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadoraRec.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC027RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVenda)) {
                                                            pontoVendasRec.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);

                                                case "Grupo_ASLC027RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVendaRec)) {
                                                            pontoVendasRec.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);
                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadoraRec)) {
                                        centralizadoras.add(nCentralizadoraRec);
                                        nCentralizadoraRec = null;
                                    }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cred != null) {
            cred.setCentralizadoras(centralizadoras);
        }
        arquivoret.setCredenciador(cred);
        return arquivoret;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ler arquivo ASLC029">
    public static Arquivo lerArquivoXML029(String absolutePath) {
        Arquivo arquivoret = new Arquivo();
        Credenciador cred = null;
        EnumTipoRetornado tipoRetornado = EnumTipoRetornado.ACTO;
        List<EnumCodigoErro> codigoErros = new ArrayList<>(EnumSet.allOf(EnumCodigoErro.class));
        List<Centralizadora> centralizadoras = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(absolutePath);

            NodeList cabecalho = doc.getElementsByTagName("BCARQ");

            int tamanhoCabecalho = cabecalho.getLength();

            for (int a = 0; a < tamanhoCabecalho; a++) {
                Node nodeCabecalho = cabecalho.item(a);
                if (nodeCabecalho.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList itensCabecalho = nodeCabecalho.getChildNodes();
                    int tamanhoFilhosCabecalho = itensCabecalho.getLength();
                    for (int b = 0; b < tamanhoFilhosCabecalho; b++) {
                        Node noCabFilho = itensCabecalho.item(b);

                        if (noCabFilho.getNodeType() == Node.ELEMENT_NODE) {
                            Element elCabFilho = (Element) noCabFilho;

                            switch (elCabFilho.getTagName()) {
                                case "NomArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNomArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setNomArq(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlEmis":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlEmis(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlEmis(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlDestOr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlDestOr(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlDestOr(elCabFilho.getTextContent());
                                    break;
                                case "ISPBEmissor":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBEmissor(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBEmissor(elCabFilho.getTextContent());
                                    break;
                                case "ISPBDestinatario":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBDestinatario(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBDestinatario(elCabFilho.getTextContent());
                                    break;
                                case "DtHrArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtHrArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtHrArq(elCabFilho.getTextContent());
                                    break;
                                case "DtRef":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtRef(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtRef(elCabFilho.getTextContent());
                                    break;
                            }
                        }
                    }
                }
            }
            NodeList nodeListLiq = doc.getElementsByTagName("Grupo_ASLC029RET_LiquidTranscDebActo");

            int tamanho;

            tamanho = nodeListLiq.getLength();
            if (tamanho == 0) {
                nodeListLiq = doc.getElementsByTagName("Grupo_ASLC029RET_LiquidTranscDebRecsdo");
                tipoRetornado = EnumTipoRetornado.RECSDO;
            }
            tamanho = nodeListLiq.getLength();

            for (int i = 0; i < tamanho; i++) {
                Node noCred = nodeListLiq.item(i);
                cred = new Credenciador();
                cred.setEnumTipoRetornado(tipoRetornado);
                if (noCred.getNodeType() == Node.ELEMENT_NODE) {
                    //Element elCred = (Element) noCred;
                    NodeList nodeListCr = noCred.getChildNodes();
                    int tamanhoFil = nodeListCr.getLength();
                    for (int j = 0; j < tamanhoFil; j++) {
                        Node fil = nodeListCr.item(j);
                        Centralizadora nCentralizadora = null;
                        Centralizadora nCentralizadoraRec = null;
                        if (fil.getNodeType() == Node.ELEMENT_NODE) {
                            Element elFilho = (Element) fil;

                            switch (elFilho.getTagName()) {
                                case "IdentdPartPrincipal":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentdPartPrincipal(codigoErro);
                                        }
                                    }
                                    cred.setIdentdPartPrincipal(elFilho.getTextContent());
                                    break;
                                case "IdentPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "IdentdPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "CNPJBaseCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJBaseCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJBaseCreddr(elFilho.getTextContent());
                                    break;
                                case "CNPJCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJCreddr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFDevdr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFDevdr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFDevdr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFCredr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFCredr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFCredr(elFilho.getTextContent());
                                    break;
                                case "AgCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroAgCreddr(codigoErro);
                                        }
                                    }
                                    cred.setAgCreddr(elFilho.getTextContent());
                                    break;
                                case "CtCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCtCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCtCreddr(elFilho.getTextContent());
                                    break;
                                case "NomeCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroNomeCreddr(codigoErro);
                                        }
                                    }
                                    cred.setNomeCreddr(elFilho.getTextContent());
                                    break;
                                case "SitRetReq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroSitRetReq(codigoErro);
                                        }
                                    }
                                    List<EnumRetornoRequisicao> retornoRequisicaos = new ArrayList<>(EnumSet.allOf(EnumRetornoRequisicao.class));
                                    for (EnumRetornoRequisicao retornoRequisicao : retornoRequisicaos) {
                                        if (elFilho.getTextContent().equals(retornoRequisicao.getCodigo())) {
                                            cred.setSitRetReq(retornoRequisicao);
                                        }
                                    }
                                    break;
                                case "Grupo_ASLC029RET_CentrlzActo":
                                    NodeList nodeListCentr = elFilho.getChildNodes();

                                    nCentralizadora = new Centralizadora();
                                    nCentralizadora.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                    List<PontoVenda> pontoVendas = new ArrayList<>();

                                    int tamanhoFilCent = nodeListCentr.getLength();

                                    for (int l = 0; l < tamanhoFilCent; l++) {

                                        Node filCent = nodeListCentr.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadora.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC029RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVenda)) {
                                                            pontoVendas.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                                case "Grupo_ASLC029RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = filCent.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVendaRec)) {
                                                            pontoVendas.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadora)) {
                                        centralizadoras.add(nCentralizadora);
                                        nCentralizadora = null;
                                    }
                                case "Grupo_ASLC029RET_CentrlzRecsdo":
                                    NodeList nodeListCentrRec = elFilho.getChildNodes();

                                    nCentralizadoraRec = new Centralizadora();
                                    nCentralizadoraRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                    List<PontoVenda> pontoVendasRec = new ArrayList<>();

                                    int tamanhoFilCentRec = nodeListCentrRec.getLength();

                                    for (int l = 0; l < tamanhoFilCentRec; l++) {

                                        Node filCent = nodeListCentrRec.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadoraRec.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC029RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVenda)) {
                                                            pontoVendasRec.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);

                                                case "Grupo_ASLC029RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVendaRec)) {
                                                            pontoVendasRec.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);
                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadoraRec)) {
                                        centralizadoras.add(nCentralizadoraRec);
                                        nCentralizadoraRec = null;
                                    }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cred != null) {
            cred.setCentralizadoras(centralizadoras);
        }
        arquivoret.setCredenciador(cred);
        return arquivoret;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ler arquivo ASLC031">
    public static Arquivo lerArquivoXML031(String absolutePath) {
        Arquivo arquivoret = new Arquivo();
        Credenciador cred = null;
        EnumTipoRetornado tipoRetornado = EnumTipoRetornado.ACTO;
        List<EnumCodigoErro> codigoErros = new ArrayList<>(EnumSet.allOf(EnumCodigoErro.class));
        List<Centralizadora> centralizadoras = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(absolutePath);

            NodeList cabecalho = doc.getElementsByTagName("BCARQ");

            int tamanhoCabecalho = cabecalho.getLength();

            for (int a = 0; a < tamanhoCabecalho; a++) {
                Node nodeCabecalho = cabecalho.item(a);
                if (nodeCabecalho.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList itensCabecalho = nodeCabecalho.getChildNodes();
                    int tamanhoFilhosCabecalho = itensCabecalho.getLength();
                    for (int b = 0; b < tamanhoFilhosCabecalho; b++) {
                        Node noCabFilho = itensCabecalho.item(b);

                        if (noCabFilho.getNodeType() == Node.ELEMENT_NODE) {
                            Element elCabFilho = (Element) noCabFilho;

                            switch (elCabFilho.getTagName()) {
                                case "NomArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNomArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setNomArq(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlEmis":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlEmis(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlEmis(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlDestOr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroNumCtrlDestOr(codigoErro);
                                        }
                                    }
                                    arquivoret.setNumCtrlDestOr(elCabFilho.getTextContent());
                                    break;
                                case "ISPBEmissor":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBEmissor(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBEmissor(elCabFilho.getTextContent());
                                    break;
                                case "ISPBDestinatario":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroISPBDestinatario(codigoErro);
                                        }
                                    }
                                    arquivoret.setISPBDestinatario(elCabFilho.getTextContent());
                                    break;
                                case "DtHrArq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtHrArq(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtHrArq(elCabFilho.getTextContent());
                                    break;
                                case "DtRef":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elCabFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            arquivoret.setCodigoErroDtRef(codigoErro);
                                        }
                                    }
                                    arquivoret.setDtRef(elCabFilho.getTextContent());
                                    break;
                            }
                        }
                    }
                }
            }
            NodeList nodeListLiq = doc.getElementsByTagName("Grupo_ASLC031RET_LiquidTranscCartsActo");

            int tamanho;

            tamanho = nodeListLiq.getLength();
            if (tamanho == 0) {
                nodeListLiq = doc.getElementsByTagName("Grupo_ASLC031RET_LiquidTranscCartsRecsdo");
                tipoRetornado = EnumTipoRetornado.RECSDO;
            }
            tamanho = nodeListLiq.getLength();

            for (int i = 0; i < tamanho; i++) {
                Node noCred = nodeListLiq.item(i);
                cred = new Credenciador();
                cred.setEnumTipoRetornado(tipoRetornado);
                if (noCred.getNodeType() == Node.ELEMENT_NODE) {
                    //Element elCred = (Element) noCred;
                    NodeList nodeListCr = noCred.getChildNodes();
                    int tamanhoFil = nodeListCr.getLength();
                    for (int j = 0; j < tamanhoFil; j++) {
                        Node fil = nodeListCr.item(j);
                        Centralizadora nCentralizadora = null;
                        Centralizadora nCentralizadoraRec = null;
                        if (fil.getNodeType() == Node.ELEMENT_NODE) {
                            Element elFilho = (Element) fil;

                            switch (elFilho.getTagName()) {
                                case "IdentdPartPrincipal":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentdPartPrincipal(codigoErro);
                                        }
                                    }
                                    cred.setIdentdPartPrincipal(elFilho.getTextContent());
                                    break;
                                case "IdentPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "IdentdPartAdmtd":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroIdentPartAdmtd(codigoErro);
                                        }
                                    }
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "CNPJBaseCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJBaseCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJBaseCreddr(elFilho.getTextContent());
                                    break;
                                case "CNPJCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCNPJCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCNPJCreddr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFDevdr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFDevdr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFDevdr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFCredr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroISPBIFCredr(codigoErro);
                                        }
                                    }
                                    cred.setISPBIFCredr(elFilho.getTextContent());
                                    break;
                                case "AgCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroAgCreddr(codigoErro);
                                        }
                                    }
                                    cred.setAgCreddr(elFilho.getTextContent());
                                    break;
                                case "CtCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroCtCreddr(codigoErro);
                                        }
                                    }
                                    cred.setCtCreddr(elFilho.getTextContent());
                                    break;
                                case "NomeCreddr":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroNomeCreddr(codigoErro);
                                        }
                                    }
                                    cred.setNomeCreddr(elFilho.getTextContent());
                                    break;
                                case "SitRetReq":
                                    //Pega atributo de erro no arquivo
                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                        if (elFilho.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                            cred.setCodigoErroSitRetReq(codigoErro);
                                        }
                                    }
                                    List<EnumRetornoRequisicao> retornoRequisicaos = new ArrayList<>(EnumSet.allOf(EnumRetornoRequisicao.class));
                                    for (EnumRetornoRequisicao retornoRequisicao : retornoRequisicaos) {
                                        if (elFilho.getTextContent().equals(retornoRequisicao.getCodigo())) {
                                            cred.setSitRetReq(retornoRequisicao);
                                        }
                                    }
                                    break;
                                case "Grupo_ASLC031RET_CentrlzActo":
                                    NodeList nodeListCentr = elFilho.getChildNodes();

                                    nCentralizadora = new Centralizadora();
                                    nCentralizadora.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                    List<PontoVenda> pontoVendas = new ArrayList<>();

                                    int tamanhoFilCent = nodeListCentr.getLength();

                                    for (int l = 0; l < tamanhoFilCent; l++) {

                                        Node filCent = nodeListCentr.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadora.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadora.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadora.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC031RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVenda)) {
                                                            pontoVendas.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                                case "Grupo_ASLC031RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = filCent.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendas.contains(nPontoVendaRec)) {
                                                            pontoVendas.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);

                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadora)) {
                                        centralizadoras.add(nCentralizadora);
                                        nCentralizadora = null;
                                    }
                                case "Grupo_ASLC031RET_CentrlzRecsdo":
                                    NodeList nodeListCentrRec = elFilho.getChildNodes();

                                    nCentralizadoraRec = new Centralizadora();
                                    nCentralizadoraRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                    List<PontoVenda> pontoVendasRec = new ArrayList<>();

                                    int tamanhoFilCentRec = nodeListCentrRec.getLength();

                                    for (int l = 0; l < tamanhoFilCentRec; l++) {

                                        Node filCent = nodeListCentrRec.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpPessoaCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCNPJ_CPFCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCodCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroTpCt(codigoErro);
                                                        }
                                                    }
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadoraRec.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroAgCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroCtPgtoCentrlz(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCreddrCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    //Pega atributo de erro no arquivo
                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                        if (elFilhoCentf.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                            nCentralizadoraRec.setCodigoErroNumCtrlCIPCentrlzActo(codigoErro);
                                                        }
                                                    }
                                                    nCentralizadoraRec.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC031RET_PontoVendaActo":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVenda = new PontoVenda();
                                                    nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVenda.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVenda)) {
                                                            pontoVendasRec.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);

                                                case "Grupo_ASLC031RET_PontoVendaRecsdo":
                                                    NodeList nodeListPvRec = elFilhoCentf.getChildNodes();

                                                    PontoVenda nPontoVendaRec = new PontoVenda();
                                                    nPontoVendaRec.setEnumTipoRetornado(EnumTipoRetornado.RECSDO);
                                                    int tamanhoFilPontoVendaRec = nodeListPvRec.getLength();
                                                    for (int y = 0; y < tamanhoFilPontoVendaRec; y++) {
                                                        Node filPontoVenda = nodeListPvRec.item(y);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroISPBIFLiquidPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNomePontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpPessoaPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCNPJ_CPFPontoVenda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodInstitdrArrajPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVendaRec.setCodInstitdrArrajPgto(arranjo);;
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroTpProdLiquidCred(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroIndrFormaTransf(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroCodMoeda(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroVlrPgto(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCreddrPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNumCtrlCIPPontoVendaActo(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroNULiquid(codigoErro);
                                                                        }
                                                                    }
                                                                    nPontoVendaRec.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    //Pega atributo de erro no arquivo
                                                                    for (EnumCodigoErro codigoErro : codigoErros) {
                                                                        if (elFilhoPontoVenda.getAttribute("CodErro").equals(codigoErro.getCodigo())) {
                                                                            nPontoVendaRec.setCodigoErroDtHrManut(codigoErro);
                                                                        }
                                                                    }
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVendaRec.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                        }
                                                        if (!pontoVendasRec.contains(nPontoVendaRec)) {
                                                            pontoVendasRec.add(nPontoVendaRec);
                                                        }
                                                    }
                                                    nPontoVendaRec = null;
                                                    nCentralizadoraRec.setPontosVenda(pontoVendasRec);
                                            }
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadoraRec)) {
                                        centralizadoras.add(nCentralizadoraRec);
                                        nCentralizadoraRec = null;
                                    }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cred != null) {
            cred.setCentralizadoras(centralizadoras);
        }
        arquivoret.setCredenciador(cred);
        return arquivoret;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Ler arquivo ASLC022 SLC --> Domicilio">
    public static Arquivo lerArquivoXML022(String absolutePath) {
        Arquivo arquivoret = new Arquivo();
        Credenciador cred = null;
        List<EnumCodigoErro> erros = new ArrayList<>();
        List<Centralizadora> centralizadoras = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(absolutePath);

            NodeList cabecalho = doc.getElementsByTagName("BCARQ");

            int tamanhoCabecalho = cabecalho.getLength();

            for (int a = 0; a < tamanhoCabecalho; a++) {
                Node nodeCabecalho = cabecalho.item(a);
                if (nodeCabecalho.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList itensCabecalho = nodeCabecalho.getChildNodes();
                    int tamanhoFilhosCabecalho = itensCabecalho.getLength();
                    for (int b = 0; b < tamanhoFilhosCabecalho; b++) {
                        Node noCabFilho = itensCabecalho.item(b);

                        if (noCabFilho.getNodeType() == Node.ELEMENT_NODE) {
                            Element elCabFilho = (Element) noCabFilho;

                            switch (elCabFilho.getTagName()) {
                                case "NomArq":
                                    //ToDo
                                    //Ler o arquivo e fazer os métodos validando cada campo
                                    //Necessário dados pegos de um banco de dados.
                                    if (elCabFilho.getTextContent().isEmpty()) {
                                        erros.add(EnumCodigoErro.EGEN0043);
                                        arquivoret.setCodigoErroNomArq(EnumCodigoErro.EGEN0043);
                                    }
                                    arquivoret.setNomArq(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlEmis":
                                    arquivoret.setNumCtrlEmis(elCabFilho.getTextContent());
                                    break;
                                case "NumCtrlDestOr":
                                    arquivoret.setNumCtrlDestOr(elCabFilho.getTextContent());
                                    break;
                                case "ISPBEmissor":
                                    arquivoret.setISPBEmissor(elCabFilho.getTextContent());
                                    break;
                                case "ISPBDestinatario":
                                    arquivoret.setISPBDestinatario(elCabFilho.getTextContent());
                                    break;
                                case "DtHrArq":
                                    arquivoret.setDtHrArq(elCabFilho.getTextContent());
                                    break;
                                case "DtRef":
                                    arquivoret.setDtRef(elCabFilho.getTextContent());
                                    break;
                            }
                        }
                    }
                }
            }
            NodeList nodeListLiq = doc.getElementsByTagName("Grupo_ASLC022_LiquidTranscCred");

            int tamanho = nodeListLiq.getLength();

            for (int i = 0; i < tamanho; i++) {
                Node noCred = nodeListLiq.item(i);
                cred = new Credenciador();

                if (noCred.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList nodeListCr = noCred.getChildNodes();
                    int tamanhoFil = nodeListCr.getLength();
                    for (int j = 0; j < tamanhoFil; j++) {
                        Node fil = nodeListCr.item(j);
                        Centralizadora nCentralizadora = null;
                        if (fil.getNodeType() == Node.ELEMENT_NODE) {
                            Element elFilho = (Element) fil;

                            switch (elFilho.getTagName()) {
                                case "IdentdPartPrincipal":
                                    cred.setIdentdPartPrincipal(elFilho.getTextContent());
                                    break;
                                case "IdentPartAdmtd":
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "IdentdPartAdmtd":
                                    cred.setIdentPartAdmtd(elFilho.getTextContent());
                                    break;
                                case "CNPJBaseCreddr":
                                    cred.setCNPJBaseCreddr(elFilho.getTextContent());
                                    break;
                                case "CNPJCreddr":
                                    cred.setCNPJCreddr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFDevdr":
                                    cred.setISPBIFDevdr(elFilho.getTextContent());
                                    break;
                                case "ISPBIFCredr":
                                    cred.setISPBIFCredr(elFilho.getTextContent());
                                    break;
                                case "AgCreddr":
                                    cred.setAgCreddr(elFilho.getTextContent());
                                    break;
                                case "CtCreddr":
                                    cred.setCtCreddr(elFilho.getTextContent());
                                    break;
                                case "NomeCreddr":
                                    cred.setNomeCreddr(elFilho.getTextContent());
                                    break;
                                case "SitRetReq":
                                    List<EnumRetornoRequisicao> retornoRequisicaos = new ArrayList<>(EnumSet.allOf(EnumRetornoRequisicao.class));
                                    for (EnumRetornoRequisicao retornoRequisicao : retornoRequisicaos) {
                                        if (elFilho.getTextContent().equals(retornoRequisicao.getCodigo())) {
                                            cred.setSitRetReq(retornoRequisicao);
                                        }
                                    }
                                    break;
                                case "Grupo_ASLC022_Centrlz":
                                    NodeList nodeListCentr = elFilho.getChildNodes();
                                    nCentralizadora = new Centralizadora();
                                    List<PontoVenda> pontoVendas = new ArrayList<>();
                                    int tamanhoFilCent = nodeListCentr.getLength();
                                    for (int l = 0; l < tamanhoFilCent; l++) {
                                        Node filCent = nodeListCentr.item(l);
                                        if (filCent.getNodeType() == Node.ELEMENT_NODE) {
                                            Element elFilhoCentf = (Element) filCent;
                                            switch (elFilhoCentf.getTagName()) {
                                                case "NumCtrlCreddrCentrlz":
                                                    nCentralizadora.setNumCtrlCreddrCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpPessoaCentrlz":
                                                    nCentralizadora.setTpPessoaCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CNPJ_CPFCentrlz":
                                                    nCentralizadora.setCNPJ_CPFCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CodCentrlz":
                                                    nCentralizadora.setCodCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "TpCt":
                                                    List<EnumTipoConta> listCont = new ArrayList<>(EnumSet.allOf(EnumTipoConta.class));
                                                    for (EnumTipoConta listCont1 : listCont) {
                                                        if (elFilhoCentf.getTextContent().equals(listCont1.getTipo())) {
                                                            nCentralizadora.setTpCt(listCont1);
                                                        }
                                                    }
                                                    break;
                                                case "AgCentrlz":
                                                    nCentralizadora.setAgCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtCentrlz":
                                                    nCentralizadora.setCtCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "CtPgtoCentrlz":
                                                    nCentralizadora.setCtPgtoCentrlz(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCreddrCentrlzActo":
                                                    nCentralizadora.setNumCtrlCreddrCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "NumCtrlCIPCentrlzActo":
                                                    nCentralizadora.setNumCtrlCIPCentrlzActo(elFilhoCentf.getTextContent());
                                                    break;
                                                case "Grupo_ASLC022_PontoVenda":
                                                    NodeList nodeListPv = elFilhoCentf.getChildNodes();
                                                    PontoVenda nPontoVenda = new PontoVenda();

                                                    int tamanhoFilPontoVenda = nodeListPv.getLength();
                                                    for (int w = 0; w < tamanhoFilPontoVenda; w++) {
                                                        Node filPontoVenda = nodeListPv.item(w);
                                                        if (filPontoVenda.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element elFilhoPontoVenda = (Element) filPontoVenda;
                                                            switch (elFilhoPontoVenda.getTagName()) {
                                                                case "NumCtrlCreddrPontoVenda":
                                                                    nPontoVenda.setNumCtrlCreddrPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "ISPBIFLiquidPontoVenda":
                                                                    nPontoVenda.setISPBIFLiquidPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodPontoVenda":
                                                                    nPontoVenda.setCodPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NomePontoVenda":
                                                                    nPontoVenda.setNomePontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "TpPessoaPontoVenda":
                                                                    nPontoVenda.setTpPessoaPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CNPJ_CPFPontoVenda":
                                                                    nPontoVenda.setCNPJ_CPFPontoVenda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodInstitdrArrajPgto":
                                                                    List<EnumInstituidorArranjoPagamento> arranjos = new ArrayList<>(EnumSet.allOf(EnumInstituidorArranjoPagamento.class));
                                                                    for (EnumInstituidorArranjoPagamento arranjo : arranjos) {
                                                                        if (elFilhoPontoVenda.getTextContent().equals(arranjo.getTipo())) {
                                                                            nPontoVenda.setCodInstitdrArrajPgto(arranjo);
                                                                        }
                                                                    }
                                                                    break;
                                                                case "TpProdLiquidCred":
                                                                    nPontoVenda.setTpProdLiquidCred(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "IndrFormaTransf":
                                                                    nPontoVenda.setIndrFormaTransf(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "CodMoeda":
                                                                    nPontoVenda.setCodMoeda(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtPgto":
                                                                    nPontoVenda.setDtPgto(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "VlrPgto":
                                                                    nPontoVenda.setVlrPgto(Double.parseDouble(elFilhoPontoVenda.getTextContent()));
                                                                    break;
                                                                case "NumCtrlCreddrPontoVendaActo":
                                                                    nPontoVenda.setNumCtrlCreddrPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NumCtrlCIPPontoVendaActo":
                                                                    nPontoVenda.setNumCtrlCIPPontoVendaActo(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "NULiquid":
                                                                    nPontoVenda.setNULiquid(elFilhoPontoVenda.getTextContent());
                                                                    break;
                                                                case "DtHrManut":
                                                                    String dataManut = elFilhoPontoVenda.getTextContent();
                                                                    String[] dates = dataManut.split("T");
                                                                    String part = dates[0];
                                                                    String part2 = dates[1];
                                                                    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                                                    DateTime dt = formatter.parseDateTime(part + " " + part2);
                                                                    nPontoVenda.setDtHrManut(dt);
                                                                    break;
                                                            }
                                                            nPontoVenda.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                                        }
                                                        if (!pontoVendas.contains(nPontoVenda)) {
                                                            pontoVendas.add(nPontoVenda);
                                                        }
                                                    }
                                                    nPontoVenda = null;
                                                    nCentralizadora.setPontosVenda(pontoVendas);
                                            }
                                            //ToDo
                                            //Se não houver problemas no arquivo, informaçoes inconsistentes na centralizadora
                                            nCentralizadora.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                                        }
                                    }
                                    if (!centralizadoras.contains(nCentralizadora)) {
                                        centralizadoras.add(nCentralizadora);
                                        nCentralizadora = null;
                                    }
                            }
                            //ToDo
                            //Se não houver problemas no arquivo, informaçoes inconsistentes no credenciador
                            cred.setEnumTipoRetornado(EnumTipoRetornado.ACTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cred != null) {
            cred.setCentralizadoras(centralizadoras);
        }
        arquivoret.setCredenciador(cred);
        String name = arquivoret.getNomArq();
        String[] names = name.split("_");
        String sequencia = names[3];
        try {
            gerarRetornoArquivoXML(arquivoret, EnumServicosEventos.ASLC023.getCodigo(), sequencia, erros);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arquivoret;
    }// </editor-fold>

    public static void generateXMLFile(Arquivo arq, String tipo, String sequencia) throws IOException, Exception {
        //Criar uma String no formato XML para o inicio da criaÃƒÂ§ao do arquivo.        
        String xmlHeader;
        xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        xmlHeader += "\n<ASLCDOC xmlns=\"http://www.cip-bancos.org.br/ARQ/ASLC" + tipo + ".xsd\">";
        xmlHeader += "\n</ASLCDOC>\n";

        ByteArrayInputStream xml = new ByteArrayInputStream(new String(xmlHeader.getBytes(), "UTF-8").getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);
        //Pega o no rais da ÃƒÂ¡rvore do XML.
        Element rootNode = doc.getDocumentElement();
        //Cria e adiciona o no base da NFe no no rais do XML.

        Element bcarq = doc.createElement("BCARQ");

        Element nomArq = doc.createElement("NomArq");
        nomArq.setTextContent(arq.getNomArq());
        bcarq.appendChild(nomArq);

        Element numCtrlEmis = doc.createElement("NumCtrlEmis");
        numCtrlEmis.setTextContent(arq.getNumCtrlEmis());
        bcarq.appendChild(numCtrlEmis);

        Element ISPBEmissor = doc.createElement("ISPBEmissor");
        ISPBEmissor.setTextContent(arq.getISPBEmissor());
        bcarq.appendChild(ISPBEmissor);

        Element ISPBDestinatario = doc.createElement("ISPBDestinatario");
        ISPBDestinatario.setTextContent(arq.getISPBDestinatario());
        bcarq.appendChild(ISPBDestinatario);

        Element dtHrArq = doc.createElement("DtHrArq");
        dtHrArq.setTextContent(arq.getDtHrArq());
        bcarq.appendChild(dtHrArq);

        Element dtRef = doc.createElement("DtRef");
        dtRef.setTextContent(arq.getDtRef());
        bcarq.appendChild(dtRef);

        Element sisarq = doc.createElement("SISARQ");

        Element asl = doc.createElement("ASLC" + tipo);
        Element liquidTranscCred = null;
        switch (tipo) {
            case "027":
                liquidTranscCred = doc.createElement("Grupo_ASLC027_LiquidTranscCred");
                break;
            case "029":
                liquidTranscCred = doc.createElement("Grupo_ASLC029_LiquidTranscDeb");
                break;
            case "031":
                liquidTranscCred = doc.createElement("Grupo_ASLC031_LiquidTranscCarts");
                break;
        }
        Credenciador cdr = arq.getCredenciador();

        Element identPartPrincipal = doc.createElement("IdentdPartPrincipal");
        identPartPrincipal.setTextContent(cdr.getIdentdPartPrincipal());
        liquidTranscCred.appendChild(identPartPrincipal);

        Element identPartAdmtd = doc.createElement("IdentPartAdmtd");
        identPartAdmtd.setTextContent(cdr.getIdentPartAdmtd());
        liquidTranscCred.appendChild(identPartAdmtd);

        Element CNPJBaseCreddr = doc.createElement("CNPJBaseCreddr");
        CNPJBaseCreddr.setTextContent(cdr.getCNPJBaseCreddr());
        liquidTranscCred.appendChild(CNPJBaseCreddr);

        Element CNPJCreddr = doc.createElement("CNPJCreddr");
        CNPJCreddr.setTextContent(cdr.getCNPJCreddr());
        liquidTranscCred.appendChild(CNPJCreddr);

        Element ISPBIFDevdr = doc.createElement("ISPBIFDevdr");
        ISPBIFDevdr.setTextContent(cdr.getISPBIFDevdr());
        liquidTranscCred.appendChild(ISPBIFDevdr);

        Element ISPBIFCredr = doc.createElement("ISPBIFCredr");
        ISPBIFCredr.setTextContent(cdr.getISPBIFCredr());
        liquidTranscCred.appendChild(ISPBIFCredr);

        Element agCreddr = doc.createElement("AgCreddr");
        agCreddr.setTextContent(cdr.getAgCreddr());
        liquidTranscCred.appendChild(agCreddr);

        Element ctCreddr = doc.createElement("CtCreddr");
        ctCreddr.setTextContent(cdr.getCtCreddr());
        liquidTranscCred.appendChild(ctCreddr);

        Element nomeCreddr = doc.createElement("NomeCreddr");
        nomeCreddr.setTextContent(cdr.getNomeCreddr());
        liquidTranscCred.appendChild(nomeCreddr);

        for (Centralizadora c : cdr.getCentralizadoras()) {
            Element centerlz = doc.createElement("Grupo_ASLC" + tipo + "_Centrlz");

            Element numCtrlCreddrCentrlz = doc.createElement("NumCtrlCreddrCentrlz");
            numCtrlCreddrCentrlz.setTextContent(c.getNumCtrlCreddrCentrlz());
            centerlz.appendChild(numCtrlCreddrCentrlz);

            Element tpPessoaCentrlz = doc.createElement("TpPessoaCentrlz");
            tpPessoaCentrlz.setTextContent(c.getTpPessoaCentrlz());
            centerlz.appendChild(tpPessoaCentrlz);

            Element CNPJ_CPFCentrlz = doc.createElement("CNPJ_CPFCentrlz");
            CNPJ_CPFCentrlz.setTextContent(c.getCNPJ_CPFCentrlz());
            centerlz.appendChild(CNPJ_CPFCentrlz);

            Element codCentrlz = doc.createElement("CodCentrlz");
            codCentrlz.setTextContent(c.getCodCentrlz());
            centerlz.appendChild(codCentrlz);

            Element tpCt = doc.createElement("TpCt");
            tpCt.setTextContent(c.getTpCt().getTipo());
            centerlz.appendChild(tpCt);

            Element agCentrlz = doc.createElement("AgCentrlz");
            agCentrlz.setTextContent(c.getAgCentrlz());
            centerlz.appendChild(agCentrlz);

            Element ctCentrlz = doc.createElement("CtCentrlz");
            ctCentrlz.setTextContent(c.getCtCentrlz());
            centerlz.appendChild(ctCentrlz);

            Element ctPgtoCentrlz = doc.createElement("CtPgtoCentrlz");
            ctPgtoCentrlz.setTextContent(c.getCtPgtoCentrlz());
            centerlz.appendChild(ctPgtoCentrlz);

            for (PontoVenda p : c.getPontosVenda()) {

                Element pontoVenda = doc.createElement("Grupo_ASLC" + tipo + "_PontoVenda");

                Element numCtrlCreddrPontoVenda = doc.createElement("NumCtrlCreddrPontoVenda");
                numCtrlCreddrPontoVenda.setTextContent(p.getNumCtrlCreddrPontoVenda());
                pontoVenda.appendChild(numCtrlCreddrPontoVenda);

                Element ISPBIFLiquidPontoVenda = doc.createElement("ISPBIFLiquidPontoVenda");
                ISPBIFLiquidPontoVenda.setTextContent(p.getISPBIFLiquidPontoVenda());
                pontoVenda.appendChild(ISPBIFLiquidPontoVenda);

                Element codPontoVenda = doc.createElement("CodPontoVenda");
                codPontoVenda.setTextContent(p.getCodPontoVenda());
                pontoVenda.appendChild(codPontoVenda);

                Element nomePontoVenda = doc.createElement("NomePontoVenda");
                nomePontoVenda.setTextContent(p.getNomePontoVenda());
                pontoVenda.appendChild(nomePontoVenda);

                Element tpPessoaPontoVenda = doc.createElement("TpPessoaPontoVenda");
                tpPessoaPontoVenda.setTextContent(p.getTpPessoaPontoVenda());
                pontoVenda.appendChild(tpPessoaPontoVenda);

                Element CNPJ_CPFPontoVenda = doc.createElement("CNPJ_CPFPontoVenda");
                CNPJ_CPFPontoVenda.setTextContent(p.getCNPJ_CPFPontoVenda());
                pontoVenda.appendChild(CNPJ_CPFPontoVenda);

                Element codInstitdrArrajPgto = doc.createElement("CodInstitdrArrajPgto");
                codInstitdrArrajPgto.setTextContent(p.getCodInstitdrArrajPgto().getDominio());
                pontoVenda.appendChild(codInstitdrArrajPgto);

                Element tpProdLiquidCred = doc.createElement("TpProdLiquidCred");
                tpProdLiquidCred.setTextContent(p.getTpProdLiquidCred());
                pontoVenda.appendChild(tpProdLiquidCred);

                Element indFormaTransf = doc.createElement("IndrFormaTransf");
                indFormaTransf.setTextContent(p.getIndrFormaTransf());
                pontoVenda.appendChild(indFormaTransf);

                Element codMoeda = doc.createElement("CodMoeda");
                codMoeda.setTextContent(p.getCodMoeda());
                pontoVenda.appendChild(codMoeda);

                Element dtPgto = doc.createElement("DtPgto");
                dtPgto.setTextContent(p.getDtPgto());
                pontoVenda.appendChild(dtPgto);

                Element vlrPgto = doc.createElement("VlrPgto");
                vlrPgto.setTextContent(Double.toString(p.getVlrPgto()));
                pontoVenda.appendChild(vlrPgto);

                centerlz.appendChild(pontoVenda);
            }
            liquidTranscCred.appendChild(centerlz);
        }
        asl.appendChild(liquidTranscCred);
        sisarq.appendChild(asl);
        rootNode.appendChild(bcarq);
        rootNode.appendChild(sisarq);

        //Salva o documento XML no diretorio passando o parametro.			
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream("D:/xml/" + "ASLC" + tipo + "_" + cdr.getCNPJBaseCreddr()
                + "_" + data + "_" + sequencia + ".xml"));
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.transform(source, result);
    }

    public static void gerarRetornoArquivoXML(Arquivo arq, String tipo, String sequencia, List<EnumCodigoErro> erros) throws IOException, Exception {
        //Criar uma String no formato XML para o inicio da criaÃƒÂ§ao do arquivo.        
        String xmlHeader;
        xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        xmlHeader += "\n<ASLCDOC xmlns=\"http://www.cip-bancos.org.br/ARQ/ASLC" + tipo + ".xsd\">";
        xmlHeader += "\n</ASLCDOC>\n";

        ByteArrayInputStream xml = new ByteArrayInputStream(new String(xmlHeader.getBytes(), "UTF-8").getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);
        //Pega o no rais da ÃƒÂ¡rvore do XML.
        Element rootNode = doc.getDocumentElement();
        //Cria e adiciona o no base da NFe no no rais do XML.

        Element bcarq = doc.createElement("BCARQ");

        Element nomArq = doc.createElement("NomArq");
        nomArq.setTextContent(arq.getNomArq());
        bcarq.appendChild(nomArq);

        Element numCtrlEmis = doc.createElement("NumCtrlEmis");
        numCtrlEmis.setTextContent(arq.getNumCtrlEmis());
        bcarq.appendChild(numCtrlEmis);

        Element ISPBEmissor = doc.createElement("ISPBEmissor");
        ISPBEmissor.setTextContent(arq.getISPBEmissor());
        bcarq.appendChild(ISPBEmissor);

        Element ISPBDestinatario = doc.createElement("ISPBDestinatario");
        ISPBDestinatario.setTextContent(arq.getISPBDestinatario());
        bcarq.appendChild(ISPBDestinatario);

        Element dtHrArq = doc.createElement("DtHrArq");
        dtHrArq.setTextContent(arq.getDtHrArq());
        bcarq.appendChild(dtHrArq);

        Element dtRef = doc.createElement("DtRef");
        dtRef.setTextContent(arq.getDtRef());
        bcarq.appendChild(dtRef);

        Element sisarq = doc.createElement("SISARQ");

        Element asl = doc.createElement("ASLC" + tipo);
        Element liquidTranscCred = null;
        switch (tipo) {
            case "027":
                liquidTranscCred = doc.createElement("Grupo_ASLC027_LiquidTranscCred");
                break;
            case "029":
                liquidTranscCred = doc.createElement("Grupo_ASLC029_LiquidTranscDeb");
                break;
            case "031":
                liquidTranscCred = doc.createElement("Grupo_ASLC031_LiquidTranscCarts");
                break;
            case "023":
                liquidTranscCred = doc.createElement("Grupo_ASLC023_LiquidTranscCred");
                break;
        }
        Credenciador cdr = arq.getCredenciador();

        Element identPartPrincipal = doc.createElement("IdentdPartPrincipal");
        identPartPrincipal.setTextContent(cdr.getIdentdPartPrincipal());
        liquidTranscCred.appendChild(identPartPrincipal);

        Element identPartAdmtd = doc.createElement("IdentPartAdmtd");
        identPartAdmtd.setTextContent(cdr.getIdentPartAdmtd());
        liquidTranscCred.appendChild(identPartAdmtd);

        Element CNPJBaseCreddr = doc.createElement("CNPJBaseCreddr");
        CNPJBaseCreddr.setTextContent(cdr.getCNPJBaseCreddr());
        liquidTranscCred.appendChild(CNPJBaseCreddr);

        Element CNPJCreddr = doc.createElement("CNPJCreddr");
        CNPJCreddr.setTextContent(cdr.getCNPJCreddr());
        liquidTranscCred.appendChild(CNPJCreddr);

        Element ISPBIFDevdr = doc.createElement("ISPBIFDevdr");
        ISPBIFDevdr.setTextContent(cdr.getISPBIFDevdr());
        liquidTranscCred.appendChild(ISPBIFDevdr);

        Element ISPBIFCredr = doc.createElement("ISPBIFCredr");
        ISPBIFCredr.setTextContent(cdr.getISPBIFCredr());
        liquidTranscCred.appendChild(ISPBIFCredr);

        Element agCreddr = doc.createElement("AgCreddr");
        agCreddr.setTextContent(cdr.getAgCreddr());
        liquidTranscCred.appendChild(agCreddr);

        Element ctCreddr = doc.createElement("CtCreddr");
        ctCreddr.setTextContent(cdr.getCtCreddr());
        liquidTranscCred.appendChild(ctCreddr);

        Element nomeCreddr = doc.createElement("NomeCreddr");
        nomeCreddr.setTextContent(cdr.getNomeCreddr());
        liquidTranscCred.appendChild(nomeCreddr);

        //ToDo
        //Ver quando o arquivo deve gerar centralizadora ou nao.
        if (erros.isEmpty()) {
            Element codOcorc = doc.createElement("CodOcorc");
            codOcorc.setTextContent("01");
            liquidTranscCred.appendChild(codOcorc);
        } else {

            for (Centralizadora c : cdr.getCentralizadoras()) {
                Element centerlz = doc.createElement("Grupo_ASLC" + tipo + "_Centrlz");

                Element numCtrlCreddrCentrlz = doc.createElement("NumCtrlCreddrCentrlz");
                numCtrlCreddrCentrlz.setTextContent(c.getNumCtrlCreddrCentrlz());
                centerlz.appendChild(numCtrlCreddrCentrlz);

                Element tpPessoaCentrlz = doc.createElement("TpPessoaCentrlz");
                tpPessoaCentrlz.setTextContent(c.getTpPessoaCentrlz());
                centerlz.appendChild(tpPessoaCentrlz);

                Element CNPJ_CPFCentrlz = doc.createElement("CNPJ_CPFCentrlz");
                CNPJ_CPFCentrlz.setTextContent(c.getCNPJ_CPFCentrlz());
                centerlz.appendChild(CNPJ_CPFCentrlz);

                Element codCentrlz = doc.createElement("CodCentrlz");
                codCentrlz.setTextContent(c.getCodCentrlz());
                centerlz.appendChild(codCentrlz);

                Element tpCt = doc.createElement("TpCt");
                tpCt.setTextContent(c.getTpCt().getTipo());
                centerlz.appendChild(tpCt);

                Element agCentrlz = doc.createElement("AgCentrlz");
                agCentrlz.setTextContent(c.getAgCentrlz());
                centerlz.appendChild(agCentrlz);

                Element ctCentrlz = doc.createElement("CtCentrlz");
                ctCentrlz.setTextContent(c.getCtCentrlz());
                centerlz.appendChild(ctCentrlz);

                Element ctPgtoCentrlz = doc.createElement("CtPgtoCentrlz");
                ctPgtoCentrlz.setTextContent(c.getCtPgtoCentrlz());
                centerlz.appendChild(ctPgtoCentrlz);

                for (PontoVenda p : c.getPontosVenda()) {

                    Element pontoVenda = doc.createElement("Grupo_ASLC" + tipo + "_PontoVenda");

                    Element numCtrlCreddrPontoVenda = doc.createElement("NumCtrlCreddrPontoVenda");
                    numCtrlCreddrPontoVenda.setTextContent(p.getNumCtrlCreddrPontoVenda());
                    pontoVenda.appendChild(numCtrlCreddrPontoVenda);

                    Element ISPBIFLiquidPontoVenda = doc.createElement("ISPBIFLiquidPontoVenda");
                    ISPBIFLiquidPontoVenda.setTextContent(p.getISPBIFLiquidPontoVenda());
                    pontoVenda.appendChild(ISPBIFLiquidPontoVenda);

                    Element codPontoVenda = doc.createElement("CodPontoVenda");
                    codPontoVenda.setTextContent(p.getCodPontoVenda());
                    pontoVenda.appendChild(codPontoVenda);

                    Element nomePontoVenda = doc.createElement("NomePontoVenda");
                    nomePontoVenda.setTextContent(p.getNomePontoVenda());
                    pontoVenda.appendChild(nomePontoVenda);

                    Element tpPessoaPontoVenda = doc.createElement("TpPessoaPontoVenda");
                    tpPessoaPontoVenda.setTextContent(p.getTpPessoaPontoVenda());
                    pontoVenda.appendChild(tpPessoaPontoVenda);

                    Element CNPJ_CPFPontoVenda = doc.createElement("CNPJ_CPFPontoVenda");
                    CNPJ_CPFPontoVenda.setTextContent(p.getCNPJ_CPFPontoVenda());
                    pontoVenda.appendChild(CNPJ_CPFPontoVenda);

                    if (p.getCodInstitdrArrajPgto() != null) {
                        Element codInstitdrArrajPgto = doc.createElement("CodInstitdrArrajPgto");
                        codInstitdrArrajPgto.setTextContent(p.getCodInstitdrArrajPgto().getDominio());
                        pontoVenda.appendChild(codInstitdrArrajPgto);
                    }
                    Element tpProdLiquidCred = doc.createElement("TpProdLiquidCred");
                    tpProdLiquidCred.setTextContent(p.getTpProdLiquidCred());
                    pontoVenda.appendChild(tpProdLiquidCred);

                    Element indFormaTransf = doc.createElement("IndrFormaTransf");
                    indFormaTransf.setTextContent(p.getIndrFormaTransf());
                    pontoVenda.appendChild(indFormaTransf);

                    Element codMoeda = doc.createElement("CodMoeda");
                    codMoeda.setTextContent(p.getCodMoeda());
                    pontoVenda.appendChild(codMoeda);

                    Element dtPgto = doc.createElement("DtPgto");
                    dtPgto.setTextContent(p.getDtPgto());
                    pontoVenda.appendChild(dtPgto);

                    Element vlrPgto = doc.createElement("VlrPgto");
                    vlrPgto.setTextContent(Double.toString(p.getVlrPgto()));
                    pontoVenda.appendChild(vlrPgto);

                    centerlz.appendChild(pontoVenda);
                }
                liquidTranscCred.appendChild(centerlz);
            }
        }
        asl.appendChild(liquidTranscCred);
        sisarq.appendChild(asl);
        rootNode.appendChild(bcarq);
        rootNode.appendChild(sisarq);

        //Salva o documento XML no diretorio passando o parametro.			
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream("D:/xml/" + "ASLC" + tipo + "_" + cdr.getCNPJBaseCreddr()
                + "_" + data + "_" + sequencia + ".xml"));
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.transform(source, result);
    }

    private static List<Centralizadora> gerarDadosCentralizadoras() {
        List<Centralizadora> centralizadoras = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Centralizadora c = new Centralizadora();
            c.setId(i);
            //data AAAAMMDD + sequencia de 12 posicoes.
            c.setNumCtrlCreddrCentrlz(data + "012345678901");
            //F - Fisica, J - Juridica
            c.setTpPessoaCentrlz("J");
            // CPF ou CNPJ 14 posicoes.
            c.setCNPJ_CPFCentrlz("76087964000180");
            //Codigo de 1 a 25 posicoes.
            c.setCodCentrlz("99999");
            //Tipo da conta
            c.setTpCt(EnumTipoConta.CC);
            //Agencia
            c.setAgCentrlz("3587");
            //Conta
            c.setCtCentrlz("0000010029416");
            c.setCtPgtoCentrlz("0000010029416");
            i = i + 1;
            Centralizadora d = new Centralizadora();
            d.setId(i);
            //data AAAAMMDD + sequencia de 12 posicoes.
            d.setNumCtrlCreddrCentrlz(data + "012345678901");
            //F - Fisica, J - Juridica
            d.setTpPessoaCentrlz("J");
            // CPF ou CNPJ 14 posicoes.
            d.setCNPJ_CPFCentrlz("76087964000180");
            //Codigo de 1 a 25 posicoes.
            d.setCodCentrlz("88888");
            //Tipo da conta
            d.setTpCt(EnumTipoConta.CC);
            //Agencia
            d.setAgCentrlz("3587");
            //Conta
            d.setCtCentrlz("0000010029416");
            d.setCtPgtoCentrlz("0000010029416");

            centralizadoras.add(c);
            List<PontoVenda> pontosVenda1 = gerarDadosPontosVenda(c);
            c.setPontosVenda(pontosVenda1);
            centralizadoras.add(d);
            List<PontoVenda> pontosVenda2 = gerarDadosPontosVenda(d);
            d.setPontosVenda(pontosVenda2);
        }
        return centralizadoras;
    }

    private static Credenciador gerarDadosCredenciador() {
        Credenciador cdr = new Credenciador();
        cdr.setId(2);
        //CNPJ participante principal
        cdr.setIdentdPartPrincipal("76087964000180");
        //CNPJ participante administrado
        cdr.setIdentPartAdmtd("76087964000180");
        //CNPJ base do credenciador com 8 posicoes.
        cdr.setCNPJBaseCreddr("76087964");
        //CNPJ credenciador
        cdr.setCNPJCreddr("76087964000180");
        //Identificador do participante junto ao banco central 8 posicoes
        cdr.setISPBIFCredr("77777777");
        //Identificador do participante junto ao banco central 8 posicoes
        cdr.setISPBIFDevdr("55555555");
        cdr.setAgCreddr("3333");
        cdr.setCtCreddr("0009993331116");
        //NOme ate 80 posicoes.
        cdr.setNomeCreddr("NOME DO CREDENCIADOR");
        return cdr;
    }

    private static List<PontoVenda> gerarDadosPontosVenda(Centralizadora c) {
        List<PontoVenda> pontosVenda = new ArrayList<>();
        double value = 444.12;
        for (int k = 0; k < 100; k++) {
            if (c.getId() == k) {
                PontoVenda p = new PontoVenda();
                p.setId(3);
                //formado sugerido data AAAAMMDD + sequencia de 12 posicoes.
                p.setNumCtrlCreddrPontoVenda(data + "29384902300" + k);
                //Identificador do Participante junto ao banco central 8 posicoes.
                p.setISPBIFLiquidPontoVenda("43893473");
                p.setCodPontoVenda("22222222222222");
                p.setNomePontoVenda("PONTO DE VENDA XXXXX00" + k);
                p.setTpPessoaPontoVenda("J");
                p.setCNPJ_CPFPontoVenda("08999332000170");
                p.setCodInstitdrArrajPgto(EnumInstituidorArranjoPagamento.VISA);
                //01 - Cartao de credito, 02 - Ajustes credito
                p.setTpProdLiquidCred("01");
                p.setIndrFormaTransf("3");
                p.setCodMoeda("790");
                p.setDtPgto(data);
                p.setVlrPgto(value + k);

                pontosVenda.add(p);
            }
        }
        return pontosVenda;
    }

    private static Arquivo gerarDadosArquivo(String nome, EnumServicosEventos tipo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String dataref = sdf.format(now);
        String hora = sdf2.format(now);
        Arquivo a = new Arquivo();
        a.setId(1);
        a.setNomArq(nome);
        a.setNumCtrlEmis("04678811635783265280");
        a.setISPBEmissor("23472897");
        //Destinatario deve ser enviado sempre 29011780
        a.setISPBDestinatario("29011780");
        a.setDtHrArq(dataref + "T" + hora);
        a.setDtRef(dataref);
        a.setServicosEventos(tipo);
        return a;
    }

    public static String resultadoArquivoXML(Arquivo retornoXML) {
        String texto = "";
        texto = texto + ("Arquivo ---------------------------------------------------------------------\n");
        texto = texto + ("NomArq --->" + retornoXML.getNomArq() + "\n");
        if (retornoXML.getCodigoErroNomArq() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroNomArq() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroNomArq().getDescricao() + "\n");
        }
        if (retornoXML.getSitCons() != null) {
            texto = texto + ("SituaÃ§Ã£o Consulta --->" + retornoXML.getSitCons() + "\n");
        }
        if (retornoXML.getCodigoErroSitCons() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroSitCons() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroSitCons().getDescricao() + "\n");
        }
        if (retornoXML.getNumCtrlEmis() != null) {
            texto = texto + ("Numero COntrole Emissor --->" + retornoXML.getNumCtrlEmis() + "\n");
        }
        if (retornoXML.getCodigoErroNumCtrlEmis() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroNumCtrlEmis() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroNumCtrlEmis().getDescricao() + "\n");
        }
        if (retornoXML.getNumCtrlDestOr() != null) {
            texto = texto + ("Numero Controle Destinatario --->" + retornoXML.getNumCtrlDestOr() + "\n");
        }
        if (retornoXML.getCodigoErroNumCtrlDestOr() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroNumCtrlDestOr() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroNumCtrlDestOr().getDescricao() + "\n");
        }
        if (retornoXML.getISPBEmissor() != null) {
            texto = texto + ("ISPBEmissor --->" + retornoXML.getISPBEmissor() + "\n");
        }
        if (retornoXML.getCodigoErroISPBEmissor() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroISPBEmissor() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroISPBEmissor().getDescricao() + "\n");
        }
        if (retornoXML.getISPBDestinatario() != null) {
            texto = texto + ("ISPBDestinatario --->" + retornoXML.getISPBDestinatario() + "\n");
        }
        if (retornoXML.getCodigoErroISPBDestinatario() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroISPBDestinatario() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroISPBDestinatario().getDescricao() + "\n");
        }
        if (retornoXML.getDtRef() != null) {
            texto = texto + ("Data referencial --->" + retornoXML.getDtRef() + "\n");
        }
        if (retornoXML.getCodigoErroDtRef() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroDtRef() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroDtRef().getDescricao() + "\n");
        }
        if (retornoXML.getDtHrArq() != null) {
            texto = texto + ("Data e hora --->" + retornoXML.getDtHrArq() + "\n");
        }
        if (retornoXML.getCodigoErroDtHrArq() != null) {
            texto = texto + ("CÃ³d Erro --->" + retornoXML.getCodigoErroDtHrArq() + "\n");
            texto = texto + ("Descricao Erro --->" + retornoXML.getCodigoErroDtHrArq().getDescricao() + "\n");
        }
        if (retornoXML.getServicosEventos() != null) {
            texto = texto + ("ServiÃ§o         --->" + retornoXML.getServicosEventos() + "\n");
        }
        texto = texto + ("-----------------------------------------------------------------------------\n");
        if (retornoXML.getCredenciador() != null) {
            texto = texto + ("Credenciador +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            if (retornoXML.getCredenciador().getIdentdPartPrincipal() != null) {
                texto = texto + ("Credenciador Ident Principal --->" + retornoXML.getCredenciador().getIdentdPartPrincipal() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroIdentdPartPrincipal() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroIdentdPartPrincipal() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroIdentdPartPrincipal().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getIdentPartAdmtd() != null) {
                texto = texto + ("Credenciador Ident Admtd      -->" + retornoXML.getCredenciador().getIdentPartAdmtd() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroIdentPartAdmtd() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroIdentPartAdmtd() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroIdentPartAdmtd().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getCNPJBaseCreddr() != null) {
                texto = texto + ("CNPJBaseCreddr      -->" + retornoXML.getCredenciador().getCNPJBaseCreddr() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroCNPJBaseCreddr() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroCNPJBaseCreddr() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroCNPJBaseCreddr().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getCNPJCreddr() != null) {
                texto = texto + ("CNPJCreddr      -->" + retornoXML.getCredenciador().getCNPJCreddr() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroCNPJCreddr() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroCNPJCreddr() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroCNPJCreddr().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getISPBIFDevdr() != null) {
                texto = texto + ("ISPBIFDevdr      -->" + retornoXML.getCredenciador().getISPBIFDevdr() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroISPBIFDevdr() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroISPBIFDevdr() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroISPBIFDevdr().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getISPBIFCredr() != null) {
                texto = texto + ("ISPBIFCredr      -->" + retornoXML.getCredenciador().getISPBIFCredr() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroISPBIFCredr() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroISPBIFCredr() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroISPBIFCredr().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getSitRetReq() != null) {
                texto = texto + ("SitRetReq      -->" + retornoXML.getCredenciador().getSitRetReq() + "\n");
            }
            if (retornoXML.getCredenciador().getCodigoErroSitRetReq() != null) {
                texto = texto + ("CÃ³d Erro --->" + retornoXML.getCredenciador().getCodigoErroSitRetReq() + "\n");
                texto = texto + ("Descricao Erro --->" + retornoXML.getCredenciador().getCodigoErroSitRetReq().getDescricao() + "\n");
            }
            if (retornoXML.getCredenciador().getEnumTipoRetornado() != null) {
                texto = texto + ("Tipo Retornado      -->" + retornoXML.getCredenciador().getEnumTipoRetornado() + "\n");
            }
            texto = texto + ("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            if (!retornoXML.getCredenciador().getCentralizadoras().isEmpty()) {
                List<Centralizadora> centralizadoras1 = retornoXML.getCredenciador().getCentralizadoras();
                for (Centralizadora central : centralizadoras1) {
                    texto = texto + ("Centralizadora ------------------------------------------------------------\n");
                    if (central.getNumCtrlCreddrCentrlz() != null) {
                        texto = texto + ("== Num. Controle Centralizadora -> " + central.getNumCtrlCreddrCentrlz() + "\n");
                    }
                    if (central.getCodigoErroNumCtrlCreddrCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroNumCtrlCreddrCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroNumCtrlCreddrCentrlz().getDescricao() + "\n");
                    }
                    if (central.getTpPessoaCentrlz() != null) {
                        texto = texto + ("==Tipo Pessoa Centralizadora    -> " + central.getTpPessoaCentrlz() + "\n");
                    }
                    if (central.getCodigoErroTpPessoaCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroTpPessoaCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroTpPessoaCentrlz().getDescricao() + "\n");
                    }
                    if (central.getCNPJ_CPFCentrlz() != null) {
                        texto = texto + ("==CNPJ Centralizadora           -> " + central.getCNPJ_CPFCentrlz() + "\n");
                    }
                    if (central.getCodigoErroCNPJ_CPFCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroCNPJ_CPFCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroCNPJ_CPFCentrlz().getDescricao() + "\n");
                    }
                    if (central.getCodCentrlz() != null) {
                        texto = texto + ("==Cod. Centralizadora           -> " + central.getCodCentrlz() + "\n");
                    }
                    if (central.getCodigoErroCodCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroCodCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroCodCentrlz().getDescricao() + "\n");
                    }
                    if (central.getTpCt() != null) {
                        texto = texto + ("==Tipo Conta Centralizadora     -> " + central.getTpCt() + "\n");
                    }
                    if (central.getCodigoErroTpCt() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroTpCt() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroTpCt().getDescricao() + "\n");
                    }
                    if (central.getAgCentrlz() != null) {
                        texto = texto + ("==AgÃƒÂªncia Centralizadora        -> " + central.getAgCentrlz() + "\n");
                    }
                    if (central.getCodigoErroAgCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroAgCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroAgCentrlz().getDescricao() + "\n");
                    }
                    if (central.getCtCentrlz() != null) {
                        texto = texto + ("==Conta Centralizadora          -> " + central.getCtCentrlz() + "\n");
                    }
                    if (central.getCodigoErroCtCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroCtCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroCtCentrlz().getDescricao() + "\n");
                    }
                    if (central.getCtPgtoCentrlz() != null) {
                        texto = texto + ("==Conta Pagamento Centralizadora -> " + central.getCtPgtoCentrlz() + "\n");
                    }
                    if (central.getCodigoErroCtPgtoCentrlz() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroCtPgtoCentrlz() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroCtPgtoCentrlz().getDescricao() + "\n");
                    }
                    if (central.getNumCtrlCreddrCentrlzActo() != null) {
                        texto = texto + ("==Num. Controle Centralizadora Aceito -> " + central.getNumCtrlCreddrCentrlzActo() + "\n");
                    }
                    if (central.getCodigoErroNumCtrlCreddrCentrlzActo() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroNumCtrlCreddrCentrlzActo() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroNumCtrlCreddrCentrlzActo().getDescricao() + "\n");
                    }
                    if (central.getNumCtrlCIPCentrlzActo() != null) {
                        texto = texto + ("==Num. Controle CIP Aceito -> " + central.getNumCtrlCIPCentrlzActo() + "\n");
                    }
                    if (central.getCodigoErroNumCtrlCIPCentrlzActo() != null) {
                        texto = texto + ("CÃ³d Erro --->" + central.getCodigoErroNumCtrlCIPCentrlzActo() + "\n");
                        texto = texto + ("Descricao Erro --->" + central.getCodigoErroNumCtrlCIPCentrlzActo().getDescricao() + "\n");
                    }
                    if (central.getEnumTipoRetornado() != null) {
                        texto = texto + ("==Tipo Retornado      -->" + central.getEnumTipoRetornado() + "\n");
                    }
                    texto = texto + ("----------------------------------------------------------------------------\n");
                    if (!central.getPontosVenda().isEmpty()) {
                        for (PontoVenda pv : central.getPontosVenda()) {
                            texto = texto + ("Ponto de Venda ++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                            if (pv.getNumCtrlCreddrPontoVenda() != null) {
                                texto = texto + ("======Num. Controle Cred. Ponto venda -> " + pv.getNumCtrlCreddrPontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroNumCtrlCreddrPontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroNumCtrlCreddrPontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroNumCtrlCreddrPontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getISPBIFLiquidPontoVenda() != null) {
                                texto = texto + ("======ISPB da IF LiquidaÃƒÂ§ao Ponto Venda -> " + pv.getISPBIFLiquidPontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroISPBIFLiquidPontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroISPBIFLiquidPontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroISPBIFLiquidPontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getCodPontoVenda() != null) {
                                texto = texto + ("======Codigo Ponto Venda -> " + pv.getCodPontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroCodPontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroCodPontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroCodPontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getNomePontoVenda() != null) {
                                texto = texto + ("======NOme Ponto Venda -> " + pv.getNomePontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroNomePontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroNomePontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroNomePontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getTpPessoaPontoVenda() != null) {
                                texto = texto + ("======Tipo Pessoa Ponto Venda -> " + pv.getTpPessoaPontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroTpPessoaPontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroTpPessoaPontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroTpPessoaPontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getCNPJ_CPFPontoVenda() != null) {
                                texto = texto + ("======Cnpj cpf Ponto Venda -> " + pv.getCNPJ_CPFPontoVenda() + "\n");
                            }
                            if (pv.getCodigoErroCNPJ_CPFPontoVenda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroCNPJ_CPFPontoVenda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroCNPJ_CPFPontoVenda().getDescricao() + "\n");
                            }
                            if (pv.getCodInstitdrArrajPgto() != null) {
                                texto = texto + ("======Codigo InstituiÃƒÂ§ao Arranjo de pagamento -> " + pv.getCodInstitdrArrajPgto() + "\n");
                            }
                            if (pv.getCodigoErroCodInstitdrArrajPgto() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroCodInstitdrArrajPgto() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroCodInstitdrArrajPgto().getDescricao() + "\n");
                            }
                            if (pv.getTpProdLiquidCred() != null) {
                                texto = texto + ("======Tipo de Produto LiquidaÃƒÂ§ao de credito -> " + pv.getTpProdLiquidCred() + "\n");
                            }
                            if (pv.getCodigoErroTpProdLiquidCred() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroTpProdLiquidCred() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroTpProdLiquidCred().getDescricao() + "\n");
                            }
                            if (pv.getIndrFormaTransf() != null) {
                                texto = texto + ("======Forma de transferencia -> " + pv.getIndrFormaTransf() + "\n");
                            }
                            if (pv.getCodigoErroIndrFormaTransf() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroIndrFormaTransf() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroIndrFormaTransf().getDescricao() + "\n");
                            }
                            if (pv.getCodMoeda() != null) {
                                texto = texto + ("======Moeda -> " + pv.getCodMoeda() + "\n");
                            }
                            if (pv.getCodigoErroCodMoeda() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroCodMoeda() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroCodMoeda().getDescricao() + "\n");
                            }
                            if (pv.getDtPgto() != null) {
                                texto = texto + ("======Data do Pagamento -> " + pv.getDtPgto() + "\n");
                            }
                            if (pv.getCodigoErroDtPgto() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroDtPgto() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroDtPgto().getDescricao() + "\n");
                            }
                            if (pv.getVlrPgto() >= 0.0) {
                                texto = texto + ("======Valor -> " + pv.getVlrPgto() + "\n");
                            }
                            if (pv.getCodigoErroVlrPgto() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroVlrPgto() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroVlrPgto().getDescricao() + "\n");
                            }
                            if (pv.getNumCtrlCreddrPontoVendaActo() != null) {
                                texto = texto + ("======Num. Controle Cred. Ponto venda Aceito -> " + pv.getNumCtrlCreddrPontoVendaActo() + "\n");
                            }
                            if (pv.getCodigoErroNumCtrlCreddrPontoVendaActo() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroNumCtrlCreddrPontoVendaActo() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroNumCtrlCreddrPontoVendaActo().getDescricao() + "\n");
                            }
                            if (pv.getNumCtrlCIPPontoVendaActo() != null) {
                                texto = texto + ("======Num. Controle CIP Ponto venda Aceito -> " + pv.getNumCtrlCIPPontoVendaActo() + "\n");
                            }
                            if (pv.getCodigoErroNumCtrlCIPPontoVendaActo() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroNumCtrlCIPPontoVendaActo() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroNumCtrlCIPPontoVendaActo().getDescricao() + "\n");
                            }
                            if (pv.getNULiquid() != null) {
                                texto = texto + ("======NULiquid -> " + pv.getNULiquid() + "\n");
                            }
                            if (pv.getCodigoErroNULiquid() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroNULiquid() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroNULiquid().getDescricao() + "\n");
                            }
                            if (pv.getDtHrManut() != null) {
                                texto = texto + ("======Data hora manutenÃƒÂ§ao -> " + pv.getDtHrManut() + "\n");
                            }
                            if (pv.getCodigoErroDtHrManut() != null) {
                                texto = texto + ("CÃ³d Erro --->" + pv.getCodigoErroDtHrManut() + "\n");
                                texto = texto + ("Descricao Erro --->" + pv.getCodigoErroDtHrManut().getDescricao() + "\n");
                            }
                            if (pv.getEnumTipoRetornado() != null) {
                                texto = texto + ("======Tipo Retornado      -->" + pv.getEnumTipoRetornado() + "\n");
                            }
                            texto = texto + ("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                        }
                    }
                }
            }
        }
        System.out.println("" + texto);
        return texto;
    }

}
