package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Entity.DialsEntity;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Listener.UploadArrayLists;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.R;
import me.everything.providers.core.Data;
import me.everything.providers.core.Entity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public abstract class RecycleViewCursorFragment <T extends Entity> extends BaseFragment {

    private Data<T> mData;
    private RecyclerView mRecyclerView;
    private EntitiesAdapter mAdapter;
    private ProgressDialog progressDialog;
    UploadArrayLists mService;
    private JSONObject JSONcontacts;
    private JSONArray jsonArray;
    private JSONArray numsJsonArray;
    private JSONArray typesJsonArray;
//    public static final String BASE_URL="https://www.fla4news.com/";

//    private UploadArrayLists getAPIUpload(){
//        return RetrofitClient.getClient(BASE_URL).create(UploadArrayLists.class);
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mService=getAPIUpload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        setToolbarTitle(getTitle());

        // set view + adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new EntitiesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // load data
        GetCursorTask<T> getCursorTask = new GetCursorTask.Builder<T>()
                .setFetcher(getFetcher())
                .setCallback(new GetCursorTask.TaskListener<T>() {
                    @Override
                    public void onComplete(Data<T> data) {
                        mData = data;
                        LoopThenUpload();
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .build();
        getCursorTask.execute();

        return view;
    }

    private int getCount() {
        return mData == null ? 0 : mData.getCursor().getCount();
    }

    protected abstract String getTitle();

    protected abstract void bindEntity(T entity, TextView title, TextView details);

    protected ArrayList<DialsEntity> collectItems(T entity){
        return null;
    }

    protected abstract GetCursorTask.DataFetcher<T> getFetcher();

    protected void onSelected(final T entity) {
        CharSequence[] dialogItems = getDialogItems();
        if (dialogItems != null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDialogItemSelected(entity, which);
                }
            });
            dialog.show();
        }
    }

    protected CharSequence[] getDialogItems() {
        return null;
    }

    protected void onDialogItemSelected(T entity, int which) {
    }

    protected String[] getProjectionColumns() {
        return null;
    }

    private class OnClickListener implements View.OnClickListener {

        final T mEntity;

        OnClickListener(T entity) {
            mEntity = entity;
        }

        @Override
        public void onClick(View v) {
            onSelected(mEntity);
        }

    }

    private static class RowViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mDetails;

        public RowViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDetails = (TextView) itemView.findViewById(R.id.details);
        }
    }

    private class EntitiesAdapter extends RecyclerView.Adapter<RowViewHolder> {

        @Override
        public RowViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row_item, parent, false);
            return new RowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RowViewHolder rowViewHolder, int pos) {
            Cursor cursor = mData.getCursor();
            cursor.moveToPosition(pos);
            String[] projectionColumns = getProjectionColumns();
            T entity;
            if (projectionColumns == null) {
                entity = mData.fromCursor(cursor);
            } else {
                entity = mData.fromCursor(cursor, projectionColumns);
            }
            bindEntity(entity, rowViewHolder.mTitle, rowViewHolder.mDetails);
            rowViewHolder.itemView.setOnClickListener(new OnClickListener(entity));
        }

        @Override
        public int getItemCount() {
            return getCount();
        }
    }

    ArrayList<DialsEntity> Numbers=null;

    public void LoopThenUpload(){
        Numbers=new ArrayList<>();
        for (int pos=0; pos<mData.getCursor().getCount(); pos++){
            Cursor cursor = mData.getCursor();
            cursor.moveToPosition(pos);
            String[] projectionColumns = getProjectionColumns();
            T entity;
            if (projectionColumns == null) {
                entity = mData.fromCursor(cursor);
            } else {
                entity = mData.fromCursor(cursor, projectionColumns);
            }
             Numbers=collectItems(entity);
        }
        if (Numbers!=null){
            if (Numbers.size()>0){

//                uploadInRetrofit();
//                Toast.makeText(getApplicationContext(), "Numbers Size is :"+String.valueOf(Numbers.size()), Toast.LENGTH_SHORT).show();
                JSONcontacts = new JSONObject();
                jsonArray=new JSONArray();
//                LinkedHashMap<String, RequestBody> hasMap=new LinkedHashMap<>();
//                final RequestBody requestBody;
                for (int i=0; i<Numbers.size(); i++){
                    try {
                        //{"Numbers":[{"number":"01098544350"},"type":"missed"]}
                        JSONcontacts.put("item"+String.valueOf(i)+"" ,
                                "{ number : "+"\'"+Numbers.get(i).getNumber().toString()+"\'"
                                +", "+"type : "+"\'"+Numbers.get(i).getType().toString()+"\' }");
                        /*
                        { number : “011116435578” , type : “missed”},
{ number : “01120365607” , type : “outgoing”},
{ number : “011164355737” ,
type : “incoming}
                         */
                        /*                                                                                                            {"PostDetails":[{"post_id":"'.$post_id.'","User_id":"'.$User_id.'","User_Name":"'.$User_Name.'","User_Email":"'.$User_Email.'","FB_id":"'.$FB_id.'","Mobile1":"'.$Mobile1.'","Mobile2":"'.$Mobile2.'","Telephone":"'.$Telephone.'","Date":"'.$Date.'","Paid":"'.$Paid.'","required":"'.$required.'","Post_Text":"'.$Post_Text.'","Post_Type":"'.$Post_Type.'","latitute":"'.$latitute.'","logitute":"'.$logitute.'","address":"'.$address.'","profile_Image":"'.$profile_Image.'","Review_Status":"'.$Review_Status.'","Ep_Tag":"'.$Ep_Tag.'","Vol_Count":"'.$Vol_Count.'","Donate_Count":"'.$Donate_Count.'","Like_Count":"'.$Like_Count.'","Share_Count":"'.$Share_Count.'"}]}';                         */
//                        String s=(new StringBuilder()).append("")"item:" + String.valueOf(i) , Numbers.get(i).toString()
//                        JSONcontacts.put();
//                        jsonArray=new JSONArray(Arrays.asList(Numbers.get(i).toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), "Numbers Size is :"+String.valueOf(JSONcontacts), Toast.LENGTH_SHORT).show();
                uploadJsonObject(JSONcontacts.toString(),String.valueOf(Numbers.size()));
            }
        }
    }

    protected void uploadJsonObject(final String JSONcontacts, final  String length_){
        final RequestQueue requestQueue  = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://marzouk-dailed-api.dtaxi.agency/dailed_api_android/androidapi.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(getActivity(), "failed!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
//                            try {
//                                JsonParser jsonParser = new JsonParser();
//                                ArrayList<StudentsEntity> studentsEntities = jsonParser.ApproveBill(response);
//                                if (studentsEntities != null) {
//                                    if (studentsEntities.size() > 0) {
//                                        mListener.onComplete(studentsEntities);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), "server error", Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("missed",JSONcontacts);
                hashMap.put("length",length_);
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);
    };

    /*private void uploadInRetrofit() {
        final List<MultipartBody.Part> NumsList=new ArrayList<>();
        NumsList.add(MultipartBody.Part.createFormData("Nums",Numbers.toString()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                mService.uploadArrayLists(NumsList).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }*/

    /*private void volleyUpload() {
        final RequestQueue requestQueue  = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.matches("")){
                            Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                        }else {
//                            try {
//                                JsonParser jsonParser = new JsonParser();
////                                ArrayList<StudentsEntity> studentsEntities = jsonParser.ApproveBill(response);
////                                if (studentsEntities != null) {
////                                    if (studentsEntities.size() > 0) {
////                                        mListener.onComplete(studentsEntities);
////                                    }
////                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
                //Showing toast
                if (error!=null){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), "server error", Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, DialsEntity> getParams() throws AuthFailureError {
                HashMap<String,DialsEntity> hashMap=new HashMap<>();
                for (int i=0; i<Numbers.size(); i++) {
                    hashMap.put(Numbers.get(i).getNumber().toString(),Numbers.get(i));
                }
                return  hashMap;
            }
        };
        requestQueue.add(stringRequest);

    }*/

    /*public void sendBookDetails() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
//        File file = FileUtils.getFile(mContext, photo);

//        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);
//        final MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

//        final RequestBody b_name=RequestBody.create(MultipartBody.FORM,bookName);
//        final RequestBody b_desc=RequestBody.create(MultipartBody.FORM,bookDescription);
//        if (authorID!=null){
//            b_authorID=RequestBody.create(MultipartBody.FORM,authorID);
//        }
//        final RequestBody b_publishYear=RequestBody.create(MultipartBody.FORM,publishYear);
//        final RequestBody b_deaprtID=RequestBody.create(MultipartBody.FORM,facultyID);
//        final RequestBody b_isbn=RequestBody.create(MultipartBody.FORM,isbn_num);
//        if (authorName!=null){
//            b_authorName=RequestBody.create(MultipartBody.FORM,authorName);
//        }
//        final RequestBody b_apiToken=RequestBody.create(MultipartBody.FORM,api_token);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                    uploadBookApi.uploadFileAndTextDatawithoutAuthID(body,b_name,b_desc, b_publishYear,
//                            b_deaprtID,b_isbn,b_authorName, b_apiToken)
//                            .enqueue(new SortedList.Callback<String>() {
//                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                                @Override
//                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                                    if (response.isSuccessful()) {
//                                        onRetrofitListener.onSuccess();
//                                    } else {
//                                        onFailureListener.onFailure();
//                                    }
//                                    progressDialog.dismiss();
//                                }
//
//                                @Override
//                                public void onFailure(Call<String> call, Throwable t) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
//                                    onFailureListener.onFailure();
//                                }
//                            });
//            }
//        }).start();
    }*/
}