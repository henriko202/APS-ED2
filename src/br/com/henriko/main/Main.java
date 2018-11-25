package br.com.henriko.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

import br.com.henriko.classes.RadixTree;

/**
 * @author Henriko In�cio Alberton, Michel Augusto de Souza <br>
 *         <br>
 *         PLANO INFAL�VEL (to te copiando Nak�o) <br>
 *         1: Pedir o caminho do arquivo <br>
 *         2: Abrir o arquivo e carregar todas as palavras com mais de 2 letras
 *         na PATRICIA Trie com as suas respectivas linhas (contar \n) <br>
 *         3: Mandar tudo pra um arquivo, ordenar alfabeticamente o arquivo em
 *         um novo, <br>
 *         4: Excluir o antigo e imprimir na tela
 */
public class Main {

	public static void main(String[] args) throws IOException {
//		para testes, comente as pr�ximas 4 linhas
		if (args.length == 0 || args.length != 1) {
			System.out.println("Execu��o: java -jar " + System.getProperty("java.class.path") + " <arquivo.txt>");
			System.exit(-1);
		}

//		declarando patterns, arvore e scanners, comentando s� pra manter a pattern (o padr�o)
//		viu oq eu fiz acima? haaaaa piadas demais... caf� t� fazendo efeito
		RadixTree<String> trie = new RadixTree<String>();
		final Pattern WHITESPACE_REGEX = Pattern.compile(" ");
		final Pattern PROHIBITED_REGEX = Pattern.compile("[^\\p{L}\\s]+");
		Scanner scanner;
		int count = 1;

//		para testes, descomente a pr�xima linha e comente a pr�xima
//		String arq = "teste.txt";
		String arq = args[0];

		scanner = new Scanner(new File(
				Paths.get(".").toAbsolutePath().normalize().toString() + System.getProperty("file.separator") + arq));

//		percorre todo o arquivo
		while (scanner.hasNextLine()) {

//			 tira os caracteres que n�o s�o alfanum�ricos/latinos (.,;/etc)
			String linhaEntrada[] = PROHIBITED_REGEX.split(scanner.nextLine());
			ArrayList<String> palavras = new ArrayList<String>();
			String vetPalavras[] = null;
			for (int j = 0; j < linhaEntrada.length; j++) {

//				 d� um split nas palavras usando espa�o, pra pegar a string e nao a linha
				vetPalavras = WHITESPACE_REGEX.split(linhaEntrada[j]);
				for (int k = 0; k < vetPalavras.length; k++) {

//					adicionando palavras numa lista pra ficar mais f�cil de se trabalhar
					palavras.add(vetPalavras[k]);
				}
			}
			for (int i = 0; i < palavras.size(); i++) {
				String insere = palavras.get(i).toUpperCase();

//				se palavra < 2 caracteres, ignorada
				if (palavras.get(i).length() > 2) {
					trie.insert(insere, insere, count);
				}
			}
			count++;
		}

//		imprime tudo bonitinho		
		System.out.println("Imprimindo as chaves, valores e linhas, de forma entend�vel:");
		System.out.println();

		trie.display();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Imprimindo valores e linhas em ordem alfab�tica:");
		System.out.println();

//		colocando valores por ordem alfab�tica em output.txt
		BufferedReader reader = new BufferedReader(new FileReader(Paths.get(".").toAbsolutePath().normalize().toString()
				+ System.getProperty("file.separator") + "out.txt"));
		Map<String, String> map = new TreeMap<String, String>();
		String line = "";
		while ((line = reader.readLine()) != null) {
			map.put(getField(line), line);
		}
		reader.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(".").toAbsolutePath().normalize().toString()
				+ System.getProperty("file.separator") + "output.txt"));
		for (String val : map.values()) {
			writer.write(val);
			writer.newLine();
		}
		scanner.close();

//		apagando out.txt, deixando apenas output.txt
		File file = new File(Paths.get(".").toAbsolutePath().normalize().toString()
				+ System.getProperty("file.separator") + "out.txt");
		file.delete();
		writer.close();

//		imprimindo conte�do de output.txt na tela
		BufferedReader br = new BufferedReader(new FileReader(Paths.get(".").toAbsolutePath().normalize().toString()
				+ System.getProperty("file.separator") + "output.txt"));
		String linha = null;
		while ((linha = br.readLine()) != null) {
			System.out.println(linha);
		}
		br.close();
	}

	private static String getField(String line) {
		return line.split(" ")[0];
	}

}
