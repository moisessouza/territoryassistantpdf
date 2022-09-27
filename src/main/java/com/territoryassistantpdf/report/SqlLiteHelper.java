package com.territoryassistantpdf.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.territoryassistantpdf.report.vo.DesignacaoVO;
import com.territoryassistantpdf.report.vo.TerritorioVO;

public class SqlLiteHelper {

	private Connection connection;

	public SqlLiteHelper (String pathFile) {

		try {
			Class.forName("org.sqlite.JDBC");  
			connection = DriverManager.getConnection("jdbc:sqlite:" + pathFile);
		} catch (Exception e) {
			e.printStackTrace();
		}  

	}

	public List<TerritorioVO> buscarTodosTerritorios(){
		
		List<TerritorioVO> territorioVOs = new ArrayList<TerritorioVO>();
		
		try { 
			
			ResultSet resultSet = null;  
			Statement statement = null;  

			statement = connection.createStatement();  
			resultSet = statement.executeQuery("select ID, COD from TERRITORIO");  
			while (resultSet.next()) {  
				Long id = resultSet.getLong("ID");
				String cod = resultSet.getString("COD");
				territorioVOs.add(new TerritorioVO(id, cod));  
			}  

			resultSet.close();  
			statement.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		
		return territorioVOs;
		
	}
	
	public List<DesignacaoVO> buscarDesignacoesPorTerritorio(Long idTerritorio) {
		
		List<DesignacaoVO> designacaoVOs = new ArrayList<DesignacaoVO>();
		
		try { 
		
		ResultSet resultSet = null;  
		Statement statement = null;  

		statement = connection.createStatement();  
		resultSet = statement.executeQuery("select ID, TIPO, NOME, DATA_INICIO, DATA_FIM "
		+ " from (select DESIGNACAO.ID as ID, TIPO, NOME, DATA_INICIO, DATA_FIM from DESIGNACAO " +
		"join DIRIGENTES on (DESIGNACAO.ID_DIRIGENTE = DIRIGENTES.ID) " +
		"where ID_TERRITORIO=" + idTerritorio + " order by DESIGNACAO.ID desc limit 25) order by ID"); 
		
		while (resultSet.next()) {
			
			String tipo = resultSet.getString("TIPO");
			String nome = resultSet.getString("NOME");
			Long dataInicio = resultSet.getLong("DATA_INICIO");
			Long dataFim = resultSet.getLong("DATA_FIM");
			String dataInicioStr = convertDataLongToString(dataInicio);
			String dataFimStr = convertDataLongToString(dataFim);
			
			designacaoVOs.add(new DesignacaoVO(tipo, nome, dataInicioStr, dataFimStr));
			
		}  

		resultSet.close();  
		statement.close();  
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return designacaoVOs;
		
		
	}

	private String convertDataLongToString(Long dataInicio) {

		if (dataInicio == null || dataInicio == 0l){
			return "";
		}
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dataInicio);
		
		Date time = c.getTime();
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		fmt.format(time);
		
		return fmt.format(time);
		
	}
	
}
