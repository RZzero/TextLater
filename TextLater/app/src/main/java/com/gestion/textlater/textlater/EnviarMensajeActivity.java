package com.gestion.textlater.textlater;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.id;
import static com.gestion.textlater.textlater.R.id.fab;
import static com.gestion.textlater.textlater.R.id.subject_TextView;
import static com.gestion.textlater.textlater.R.string.Asunto;

public class EnviarMensajeActivity extends AppCompatActivity {

    boolean asunto, timeEdited, dateEdited;
    int year_x, month_x, day_x, hour_y, min_y, sec_y;
    static final int DIALOG_ID = 0, DIALOG_ID2 = 1;

    EditText mAsunto, mDestinatario, mMensaje;
    Message Mensaje;
    String mUsuario, mPlatform, mDate, id, datePI, hours;
    ImageButton date, hour;

    //Listeners for the Dialogs to get the input from the user
    //Date listener
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;
            giveDateFormat();
            dateEdited = true;
        }
    };
    //Time listener
    private TimePickerDialog.OnTimeSetListener tpickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_y = hourOfDay;
            min_y = minute;
            giveTimeFormat();
            timeEdited = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);

        asunto = true;
        timeEdited = false;
        dateEdited = false;

        if (getIntent() != null) {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            if (id.equals("telegram")) {
                asunto = false;
                mPlatform = "T";
            } else {
                mPlatform = "G";
            }
            setTitle(id.toUpperCase());
        }

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        hour_y = cal.get(Calendar.HOUR_OF_DAY);
        min_y = cal.get(Calendar.MINUTE);

        //Oculta el elemento de editar texto
        setAsunto();

        Mensaje = new Message();
        createButtons();
    }

    private void createButtons() {
        //FLOATINGBUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //The asunto :v
                mAsunto = (EditText) findViewById(R.id.Mensaje_asunto_editText);

                //TODO: CHECK IF THIS IS THE CORRECT METHOD
                if (mAsunto.isEnabled()) {
                    Mensaje.setSubject(mAsunto.getText().toString());
                }

                if (timeEdited && dateEdited) {
                    //SET THE MESSAGE PROPERTIES
                    getValues();

                    final Calendar cal = Calendar.getInstance();

                    if (year_x < cal.get(Calendar.getInstance().YEAR)) {
                        Toast.makeText(getApplicationContext(), "Año invalido.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (month_x < cal.get(Calendar.getInstance().MONTH)) {
                            Toast.makeText(getApplicationContext(), "Mes invalido.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (day_x < cal.get(Calendar.getInstance().DAY_OF_MONTH)) {
                                Toast.makeText(getApplicationContext(), "Dia invalido.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (hour_y < cal.get(Calendar.getInstance().HOUR_OF_DAY)) {
                                    Toast.makeText(getApplicationContext(), "Hora invalida.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (min_y < cal.get(Calendar.getInstance().MINUTE)) {
                                        Toast.makeText(getApplicationContext(), "Minuto invalido.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //POST THE MESSAGE
                                        if (!mAsunto.getText().equals("") && !mDestinatario.getText().equals("") && !mMensaje.getText().equals("")) {
                                            MakeHttpRequest();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Rellene los campos.", Toast.LENGTH_SHORT).show();
                                        }
                                        //clear();
                                    }
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Chouse and hour and a date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //DATeIMAGEBUTTON
        date = (ImageButton) findViewById(R.id.date_viewButton);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        //HOUrIMAGEBUTTON
        hour = (ImageButton) findViewById(R.id.hour_viewButton);

        hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID2);
            }
        });


    }

    private void giveDateFormat() {
        datePI = year_x + "-";
        datePI += month_x + "-";
        datePI += day_x + " ";
    }

    private void giveTimeFormat() {
        hours = hour_y + ":";
        hours += min_y + "";
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        } else if (id == DIALOG_ID2) {
            return new TimePickerDialog(this, tpickerListener, hour_y, min_y, true);
        }
        return null;
    }

    private void clear() {
        mAsunto.setText("");
        mDestinatario.setText("");
        mMensaje.setText("");

    }

    private void setAsunto() {
        EditText Asunto = (EditText) findViewById(R.id.Mensaje_asunto_editText);
        if (!asunto) {
            Asunto.setVisibility(View.GONE);
        }
    }

    private void MakeHttpRequest() {

          /* @platform
        * @sender
        * @ToM
        * @subject
        * @content
        * @timeToSend
        * @messageStatus
        * */
        RequestParams params = new RequestParams();
        params.put("platform", Mensaje.getPlatform().toString());
        params.put("sender", Mensaje.getSender().toString());
        params.put("ToM", Mensaje.getToM().toString());
        params.put("subject", Mensaje.getSubject().toString());
        params.put("content", Mensaje.getContent().toString());
        params.put("timeToSend", Mensaje.getTimeToSend().toString());
        params.put("messageStatus", Mensaje.getMessageStatus().toString());


        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://52.36.200.87:80/api/v1/message/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("holiboli:", headers.toString());
                Log.e("GOLA:", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void getValues() {
        mDestinatario = (EditText) findViewById(R.id.destinatario_editText);
        Mensaje.setToM(mDestinatario.getText().toString());

        mMensaje = (EditText) findViewById(R.id.Mensaje_editText);
        Mensaje.setContent(mMensaje.getText().toString());

        mUsuario = "the.robert.007@gmail.com";
        Mensaje.setSender(mUsuario);

        mDate = datePI + hours;
        Mensaje.setTimeToSend(mDate);

        //TODO: NO SE QUE PONER
        Mensaje.setMessageStatus("NS");
        Mensaje.setPlatform(mPlatform);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings_2) {
            return true;
        } else if (id == R.id.action_settings_cerrar_envioMensaje) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
