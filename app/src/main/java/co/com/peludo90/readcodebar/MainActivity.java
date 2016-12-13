package co.com.peludo90.readcodebar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView txtCedula;
    TextView txtNombre;
    TextView txtApellido;
    TextView txtFechaNacimiento;
    TextView txtGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCedula = (TextView) findViewById(R.id.txt_cedula);
        txtNombre = (TextView) findViewById(R.id.txt_nombre);
        txtApellido = (TextView) findViewById(R.id.txt_apellido);
        txtFechaNacimiento = (TextView) findViewById(R.id.txt_fecha_nacimiento);
        txtGenero = (TextView) findViewById(R.id.txt_genero);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        String result = null;
        String title = null;
        if (scanningResult != null && intent != null) {
            String scanContent = scanningResult.getContents();
            Pattern patternCedula = Pattern.compile("(?=(?=(?=(?=^\\d{8,})(^.{48}\\d{8,}))(^.{58}[A-Z]{2,}))(^.{104}[A-Z]{2,}))(^.{150}0[MF]\\d{8}).*", Pattern.DOTALL);
            if (patternCedula.matcher(scanContent).matches()) {
                setVisibilityAll(View.VISIBLE);
                String cedula = scanContent.substring(48, 58);
                String apellido1 = scanContent.substring(58, 81);
                String apellido2 = scanContent.substring(81, 104);
                String nombre1 = scanContent.substring(104, 127);
                String nombre2 = scanContent.substring(127, 149);
                String genero = scanContent.substring(151, 152);
                String fechaNamiento = getString(R.string.formato_fecha,
                        scanContent.substring(152, 156), scanContent.substring(156, 158), scanContent.substring(158, 160));

                cedula = cedula.replaceAll("^[0]+", "");
                apellido1 = apellido1.replaceAll("\\uFFFD", "");
                apellido2 = apellido2.replaceAll("\\uFFFD", "");
                nombre1 = nombre1.replaceAll("\\uFFFD", "");
                nombre2 = nombre2.replaceAll("\\uFFFD", "");

                txtCedula.setText(getString(R.string.cedula, cedula));
                txtApellido.setText(getString(R.string.apellidos, apellido1.trim() + " " + apellido2.trim()));
                txtNombre.setText(getString(R.string.nombres, nombre1.trim() + " " + nombre2.trim()));
                txtGenero.setText(getString(R.string.genero,
                        getString(genero.equals("M") ? R.string.masculino : R.string.femenino)));

                txtFechaNacimiento.setText(getString(R.string.fecha_nacimiento, fechaNamiento));
            } else {
                setVisibilityAll(View.GONE);
                txtCedula.setText(getString(R.string.no_cedula));
            }
        }
    }

    private void setVisibilityAll(int visibility) {
        txtCedula.setVisibility(visibility == View.GONE ? View.VISIBLE : visibility);
        txtNombre.setVisibility(visibility);
        txtApellido.setVisibility(visibility);
        txtFechaNacimiento.setVisibility(visibility);
        txtGenero.setVisibility(visibility);
    }
}
