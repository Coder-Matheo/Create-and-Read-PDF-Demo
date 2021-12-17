package rob.createandreadpdfdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText pdfEditText;
    TextView pdfTextView;
    Button createButton;
    Button readButton;

    private String stringPath = Environment.getExternalStorageDirectory().getPath() + "/Download/ProgrammerWorld.pdf";
    private File file = new File(stringPath);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfEditText = findViewById(R.id.pdfEditText);
        pdfTextView = findViewById(R.id.pdfTextView);

        createButton = findViewById(R.id.createButton);
        readButton = findViewById(R.id.readButton);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Paint paint = new Paint();
                String stringPDF = pdfEditText.getText().toString();


                int x = 10, y = 25;

                for (String line : stringPDF.split("\n")){
                    page.getCanvas().drawText(line, x, y, paint);

                    y+= paint.descent()- paint.ascent();
                }

                pdfDocument.finishPage(page);

                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                }catch (Exception e){
                    e.printStackTrace();
                    pdfTextView.setText("Error in Creating");
                }

                pdfDocument.close();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try{
                    PdfReader reader = new PdfReader(file.getPath());
                    String stringParse = PdfTextExtractor.getTextFromPage(reader, 1).trim();
                    reader.close();
                    pdfTextView.setText(stringParse);

                }catch(Exception e){
                    e.printStackTrace();
                    pdfTextView.setText("Error in Reading");
                }
            }
        });
    }
}