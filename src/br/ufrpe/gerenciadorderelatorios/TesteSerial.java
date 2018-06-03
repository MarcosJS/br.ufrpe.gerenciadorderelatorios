package br.ufrpe.gerenciadorderelatorios;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import br.ufrpe.gerenciadorderelatorios.model.Estrutura;

public class TesteSerial {
	public static void main(String args[]) {

		TesteSerial obj = new TesteSerial();

		Estrutura estr = new Estrutura(null, "testeSerial", "serializado", null);

		obj.serialize(estr);
		
		Estrutura estrSer = (Estrutura) obj.deserialze("c:/workspace/serializado.ser");
		
		System.out.println(estrSer.obterRaiz());

	}

	
	public void serialize(Serializable es) {

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream("c:/workspace/serializado.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(es);

			System.out.println("Done");

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public Serializable deserialze(String filename) {

		Serializable des = null;

		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = new FileInputStream(filename);
			ois = new ObjectInputStream(fin);
			des = (Serializable) ois.readObject();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return des;

	}

}

