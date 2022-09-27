package com.territoryassistantpdf.report;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.territoryassistantpdf.report.vo.DesignacaoVO;
import com.territoryassistantpdf.report.vo.TerritorioVO;

public class Main {

	private static SqlLiteHelper db; 

	private static String caminhoArquivo;
	private static String caminhoGerado;
	
	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		
		System.out.println(); 
		
		JFrame mainFrame = new JFrame("Gerar arquivo");
		mainFrame.setSize(430,170);
		mainFrame.setResizable(false);
		
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    

		JLabel arquivoLabel = new JLabel("Selecione o arquivo exportado do Territory Assistant", JLabel.CENTER);
		final JTextField caminhoArquivoField = new JTextField();
		caminhoArquivoField.setEditable(false);
		caminhoArquivoField.setColumns(20);
		JButton arquivoButton = new JButton("Selecionar");
		
		arquivoButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				final JFrame mainFrame = new JFrame("Escolha o arquivo");
				mainFrame.setSize(600,600);
				mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
				
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"DB", "db");
				chooser.setFileFilter(filter);
				
				mainFrame.add(chooser);
				mainFrame.setVisible(true);
				
				chooser.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						if (e.getActionCommand().equals("ApproveSelection")){
							caminhoArquivo = ((JFileChooser)e.getSource()).getSelectedFile().getAbsolutePath();
							caminhoArquivoField.setText(caminhoArquivo);
							mainFrame.setVisible(false);
						} else {
							mainFrame.setVisible(false);
						}
					}
				});
				
			}
		});
		
		JLabel geradoLabel = new JLabel("Selecione caminho onde deseja que o arquivo seja gerado", JLabel.CENTER);
		final JTextField caminhoGeradoField = new JTextField();
		caminhoGeradoField.setColumns(20);
		caminhoGeradoField.setEditable(false);
		JButton caminhoButton = new JButton("Selecionar");
		
		caminhoButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				final JFrame mainFrame = new JFrame("Escolha o local para geração");
				mainFrame.setSize(600,600);
				mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
				
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setSelectedFile(new File("./relatorio.pdf"));

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"PDF", "pdf");
				
				chooser.setFileFilter(filter);
				
				mainFrame.add(chooser);
				mainFrame.setVisible(true);
				
				chooser.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						if (e.getActionCommand().equals("ApproveSelection")){
							String path = ((JFileChooser)e.getSource()).getSelectedFile().getAbsolutePath();
							if (path.endsWith(".pdf")){
								caminhoGerado = path;
							} else {
								caminhoGerado = path + File.separator + "relatorio.pdf";
							}
							caminhoGeradoField.setText(caminhoGerado);
							mainFrame.setVisible(false);
						} else {
							mainFrame.setVisible(false);
						}
					}
				});
				
			}
		});
		
		JButton gerarButton = new JButton("Gerar arquivo");
		
		gerarButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (caminhoArquivo == null || caminhoArquivo.isEmpty()
						|| caminhoGerado == null || caminhoGerado.isEmpty()){
					return;
				}
				
				gerarRelatorio(caminhoArquivo, caminhoGerado);
				final JFrame sucessoFrame = new JFrame("Sucesso!!");
				sucessoFrame.setSize(250,100);
				sucessoFrame.setLocation(dim.width/2-sucessoFrame.getSize().width/2, dim.height/2-sucessoFrame.getSize().height/2);
				sucessoFrame.setResizable(false);
				JPanel controlPanel = new JPanel();
				controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
				controlPanel.setBounds(new Rectangle(0, 0, 100, 300));
				
				JLabel geradoLabel = new JLabel("Arquivo gerado com sucesso!!!", JLabel.CENTER);
				controlPanel.add(geradoLabel);
				JButton botao = new JButton("OK");
				botao.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						sucessoFrame.setVisible(false);
						try {
							Desktop.getDesktop().open(new File(caminhoGerado));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.exit(0);
					}
				});
				controlPanel.add(botao);
				sucessoFrame.add(controlPanel);
				sucessoFrame.setVisible(true);
			}
		});

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		controlPanel.setBounds(new Rectangle(0, 0, 100, 300));

		controlPanel.add(arquivoLabel);
		controlPanel.add(caminhoArquivoField);
		controlPanel.add(arquivoButton);
		controlPanel.add(geradoLabel);
		controlPanel.add(caminhoGeradoField);
		controlPanel.add(caminhoButton);
		controlPanel.add(gerarButton);
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
		
	}


	private static void gerarRelatorio(String arquivoDB, String arquivoDestino) {

		db = new SqlLiteHelper(arquivoDB);

		List<TerritorioVO> territorioVOs = db.buscarTodosTerritorios();

		PDDocument doc = null;
		try{

			doc = new PDDocument();
			PDDocument baseDocument = PDDocument.load(new File(getCaminhoArquivoModelo()));
			PDPage page = (PDPage)baseDocument.getDocumentCatalog().getAllPages().get(0);
			doc.addPage(page);
			PDPageContentStream content = new PDPageContentStream(doc, page, true, false);
			
			// De 5 em 5 se cria uma nova pagina
			float coluna = 0;

			for (TerritorioVO territorioVO : territorioVOs) {

				if (coluna != 0 && coluna % 5 == 0){
					content.close();
					baseDocument = PDDocument.load(new File(getCaminhoArquivoModelo()));
					page = (PDPage)baseDocument.getDocumentCatalog().getAllPages().get(0);
					doc.addPage(page);
					content = new PDPageContentStream(doc, page, true, false);
					coluna = 0;
				}

				preencherColuna(coluna, territorioVO, doc, page, content);

				coluna++;
				System.out.println(territorioVO.getCod());

			}

			content.close();

			doc.save(arquivoDestino);

		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Exception");
		}
	}

	private static String getCaminhoArquivoModelo() throws URISyntaxException{
		String absolute = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		absolute = absolute.substring(0, absolute.length() - 1);
		absolute = absolute.substring(0, absolute.lastIndexOf(File.separator) + 1);
		String caminho = absolute + "modelo_registro_territorio.pdf";
		System.out.println(caminho);
		return caminho;
	}
	
	private static String getCaminhoArquivoModeloDebug() throws URISyntaxException{
		return "modelo_registro_territorio.pdf";
	}
	
	private static void preencherColuna(float coluna, TerritorioVO territorioVO, PDDocument doc, PDPage page, PDPageContentStream content) throws IOException, FileNotFoundException {

		float colunaCabecalho = 80;
		float proximaColuna = 107;
		float colunaAtual = colunaCabecalho + (coluna * proximaColuna);

		PDFont font = PDType1Font.HELVETICA_BOLD;

		content.beginText();
		content.setFont( font, 9 );
		content.moveTextPositionByAmount( colunaAtual, 767 );
		content.drawString(territorioVO.getCod());
		content.endText();

		List<DesignacaoVO> designacoaVOs = db.buscarDesignacoesPorTerritorio(territorioVO.getId());

		float linha = 0;

		for (DesignacaoVO designacaoVO : designacoaVOs) {
			preencherLinhaColuna(coluna, linha, designacaoVO, doc, page, content);
			linha++;
		}
		
	}

	private static void preencherLinhaColuna(float coluna, float linha,
			DesignacaoVO designacaoVO, PDDocument doc, PDPage page, PDPageContentStream content) throws IOException, FileNotFoundException {

		float pularColuna = 106.5f;

		float posColunaOferta = 52 + (coluna * pularColuna);
		float posColunaDirigente = 70 + (coluna * pularColuna);
		float posColunaDataInicio = 44 + (coluna  * pularColuna);
		float posColunaDataFim = 99 + (coluna * pularColuna);

		float pularLinha = 13.27f;

		float linhaInicial = 767 - 14;
		float linhaAtual = linhaInicial;

		linhaAtual = linhaInicial - (linha * (pularLinha * 2));

		PDFont font = PDType1Font.HELVETICA;

		// Primeira linha

		//Oferta/Revista
		content.beginText();
		content.setFont( font, 8 );
		content.moveTextPositionByAmount(posColunaOferta, linhaAtual);
		content.drawString(designacaoVO.getTipo());
		content.endText();

		// Nome Dirigente
		content.beginText();
		content.setFont( font, 8 );
		content.moveTextPositionByAmount( posColunaDirigente, linhaAtual );
		content.drawString(designacaoVO.getNomeDirigente());
		content.endText();

		linhaAtual-=pularLinha;

		// Data inicio
		content.beginText();
		content.setFont( font, 8 );
		content.moveTextPositionByAmount( posColunaDataInicio, linhaAtual );
		content.drawString(designacaoVO.getDataInicio());
		content.endText();

		// Data Fim
		content.beginText();
		content.setFont( font, 8 );
		content.moveTextPositionByAmount( posColunaDataFim, linhaAtual );
		content.drawString(designacaoVO.getDataFim());
		content.endText();

	}

}
