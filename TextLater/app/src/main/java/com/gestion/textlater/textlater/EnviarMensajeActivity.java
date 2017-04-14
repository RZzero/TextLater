package com.gestion.textlater.textlater;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.InputStreamEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.attr.theme;
import static android.R.id.message;
import static com.gestion.textlater.textlater.R.id.fab;
import static com.gestion.textlater.textlater.R.id.subject_TextView;
import static com.gestion.textlater.textlater.R.string.Asunto;

public class EnviarMensajeActivity extends AppCompatActivity {
    static String nameID;
    boolean asunto, timeEdited, dateEdited;
    int year_x, month_x, day_x, hour_y, min_y, sec_y;
    static final int DIALOG_ID = 0, DIALOG_ID2 = 1;
    private String[] filesPath;
    private File[] filesStream;

    EditText mAsunto, mDestinatario, mMensaje;
    Message Mensaje;
    String mUsuario, mPlatform, mDate, id, datePI, hours;
    ImageButton date, hour;

    //Fileviewdialog
    private FilePickerDialog dialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String[][] myDataset = null;

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

    public EnviarMensajeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);

        filesPath = new String[]{"empty"};
        filesStream = new File[]{};
        //FILE

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new String[][]{{}};

        myDataset[0] = new String[]{};

        mAdapter = new FileAdapter(myDataset[0]);
        mRecyclerView.setAdapter(mAdapter);


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
                                    if (hour_y == cal.get(Calendar.getInstance().HOUR_OF_DAY) && min_y < cal.get(Calendar.getInstance().MINUTE)) {
                                        Toast.makeText(getApplicationContext(), "Minuto invalido.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //POST THE MESSAGE
                                        if (!mAsunto.getText().equals("") && !mDestinatario.getText().equals("") && !mMensaje.getText().equals("")) {
                                            if (filesStream.length > 0) {
                                                MakeFirstHttpRequest();
                                            } else {
                                                MakeHttpRequest();
                                            }

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

    private void MakeSecondHttpRequest(String id) {

        for (int i = 0; i < filesStream.length; i++) {
            File file = new File(filesPath[i]);
            nameID = id;
            Log.e("id:", nameID);
            Log.e("NAME FILE:", file.getName());
            new RetrieveFeedTask().execute(file);
        }

    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    class RetrieveFeedTask extends AsyncTask<File, Void, Void> {

        private Exception exception;
        String url = "http://52.36.200.87:80/api/v1/upload/"; //"http://posttestserver.com/post.php?dir=holiboli";
        private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        private final MediaType MEDIA_TYPE_SOMETHING = MediaType.parse("");
        private final OkHttpClient client = new OkHttpClient();

        protected Void doInBackground(File... file) {
            try {
                Log.e("name did change?:", nameID);
                String nameFile = nameID +"--"+file[0].getName();
                Log.e("name did change?:", nameFile);

                MediaType MEDIA_TYPE_SOMETHING = MediaType.parse(getMimeType(file[0].getPath()));
                // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", nameFile,
                                RequestBody.create(MEDIA_TYPE_SOMETHING, file[0]))
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.36.200.87:80/api/v1/upload/")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    private void MakeFirstHttpRequest() {

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
                finish();
                Log.d("holiboli:", headers.toString());
                String str = "";
                String idname = "NONNULL";
                try {
                    str = new String(response, "UTF-8");
                    JSONArray idArray = new JSONArray(str);
                    if (idArray.length() > 0) {
                        JSONObject idObject = (JSONObject) idArray.get(0);
                        idname = idObject.getString("idMessage");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("GOLA:", str);
                MakeSecondHttpRequest(idname);
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
                finish();
                Log.d("holiboli:", headers.toString());
                String str = "";
                try {
                    str = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("GOLA:", str);
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

        if (id == R.id.action_settings_cerrar_envioMensaje) {
            finish();
        } else {

            //MENU PARA SELECCIONAR LOS ARCHIVOS, Y ATRIBUTOS PARA LA SELECCION
            DialogProperties properties = new DialogProperties();
            //MULTIPLE SELECTION
            properties.selection_mode = DialogConfigs.MULTI_MODE;
            properties.selection_type = DialogConfigs.FILE_SELECT;
            properties.root = new File(DialogConfigs.DEFAULT_DIR);
            properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
            properties.offset = new File(DialogConfigs.DEFAULT_DIR);
            properties.extensions = null;

            //Muetra el cuadro de seleccion de archivos
            dialog = new FilePickerDialog(this, properties);
            dialog.setTitle("Select a File");
            dialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(String[] files) {
                    //files is the array of the paths of files selected by the Application User.
                    filesPath = files; //Obtengo el arreglo de las rutas relativas de cada archivo

                    //Arraylist temporal para verificar cuales archivos son validos y cuales no.
                    ArrayList<File> temp = new ArrayList<>();
                    //variable para contar los archivos que no fueron seleccionados.
                    int archivosNoAdmitidos = 0;

                    //Nombre de los archivos
                    String[] tempFiles = new String[filesPath.length];

                    for (int i = 0; i < tempFiles.length; i++) {

                        //Archivo temporal
                        File arc = new File(Uri.parse(filesPath[i]).getPath());

                        //conversion a MB del tamaño del archivo y verificacion de que no exeda los 10 MB.
                        if (((((float) arc.length()) / (float) 1024) / (float) 1024) <= 10.0) {
                            //agregar el archivo al arreglo temporal
                            temp.add(arc);
                        } else {
                            //aumentar contador de archivos invalidos
                            archivosNoAdmitidos++;
                        }
                        //guarda el nombre de los archvis
                        tempFiles[i] = arc.getName();
                        //log de prueba
                        Log.d("file:", arc.getName());
                    }

                    //Inicializa el arreglo de archivos
                    filesStream = new File[temp.size()];

                    //asigna los archivos al arreglo
                    for (int i = 0; i < temp.size(); i++) {
                        filesStream[i] = temp.get(i);
                    }

                    //Asigna el arreglo de nombre de archivos para el adapter para el recyclerview
                    myDataset[0] = tempFiles;

                    //Crea el adaptador
                    mAdapter = new FileAdapter(myDataset[0]);
                    //asigna el contenido al recyclerview
                    mRecyclerView.setAdapter(mAdapter);
                    //Hace visible los cambios
                    mAdapter.notifyDataSetChanged();

                    //mensaje de retroalimentacion al usuario
                    String toastMessage = filesPath.length + " archivos seleccionados";
                    //Notifica si hubo algun archivo no permitido
                    if (archivosNoAdmitidos > 0) {
                        toastMessage += ", " + archivosNoAdmitidos + "archivos sobrepasan los 10MB.";
                    }

                    //Muestra el mensaje
                    Toast.makeText(EnviarMensajeActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (dialog != null) {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                } else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(EnviarMensajeActivity.this, "Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
