import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;


public class TextOutputStream extends OutputStream {
	private JTextArea text;
	
	public TextOutputStream(JTextArea text){
		this.text = text;
	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		text.append(String.valueOf((char)b));
		text.setCaretPosition(text.getDocument().getLength());
	}

}
