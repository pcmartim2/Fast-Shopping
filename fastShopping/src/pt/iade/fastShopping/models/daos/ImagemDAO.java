package pt.iade.fastShopping.models.daos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImagemDAO {
	
	/**
	 * Metodo para converter InPutStream para byte[]
	 * @param is imagem em InPutStream
 	 * @return da imagem em byte array
	 * @throws IOException exception
	 */
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[0xFFFF];
		for (int len = is.read(buffer); len != -1; len = is.read(buffer)) { 
			os.write(buffer, 0, len);
		}
		return os.toByteArray();
	}

}
