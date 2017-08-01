package com.example.upcomingmoviesapp.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.upcomingmoviesapp.R;
import com.example.upcomingmoviesapp.interfaces.VolleyCallBack;
import com.example.upcomingmoviesapp.interfaces.VolleyStringCallBack;
import com.example.upcomingmoviesapp.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.upcomingmoviesapp.webservice.VolleySingleton.getInstance;

public class VolleyWebServices {
    private Context context;
    private String url_validate = "@#&=*+-_.,:!?()/~'%";
    private VolleySingleton requestQueue;
    private VolleyCallBack listner;
    VolleyStringCallBack stringListner;

    public VolleyStringCallBack getStringListner() {
        return stringListner;
    }

    public void setStringListner(VolleyStringCallBack stringListner) {
        this.stringListner = stringListner;
    }


    public VolleyCallBack getListner() {
        return listner;
    }

    public void setListner(VolleyCallBack listner) {
        this.listner = listner;
    }

    public void MyJsonObjectRequestGet(final Context mContext,
                                       final CoordinatorLayout cdLayout,
                                       final String methodUrl,
                                       final String methodName) {

        if (Util.isInternetAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);

            Log.e("VolleyWebServices====", "REQUEST===" + final_url);

            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {
                                Log.e("VolleyWebServices====", "RESPONSE==" + completeObject.toString());
                                JSONObject response_jsonObject = completeObject.getJSONObject(context.getString(R.string.response));
                                String status = response_jsonObject.getString(context.getString(R.string.Status));
                                if (status.equalsIgnoreCase(context.getString(R.string.succeed))) {
                                    JSONArray responseToReturn = response_jsonObject.getJSONArray(context.getString(R.string.data));
                                    listner.getResponse(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase(context.getString(R.string.failed))) {
                                    Log.e("VolleyWebServices===", "RESPONSE=====" + methodName + "" + completeObject.toString());
                                    JSONObject failedJsonObject = response_jsonObject.getJSONObject(context.getString(R.string.data));
                                    listner.displayFailedResponse(failedJsonObject.getString(context.getString(R.string.description)));
                                    Snackbar snackbar = Snackbar.make(cdLayout, failedJsonObject.getString(context.getString(R.string.description)), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            Log.e("VolleyWebServices====", "REQUEST===" + final_url);
                            Log.e("VolleyWebServices===", "inside Response.ErrorListener()===" + volleyError.toString());

                            Snackbar snackbar = Snackbar.make(cdLayout, mContext.getString(R.string.unable_to_process), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            listner.getError(volleyError.toString());

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, mContext.getString(R.string.no_internet), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    public void MyJsonString(final Context mContext,
                             final CoordinatorLayout cdLayout,
                             final String methodUrl,
                             final String methodName) {

        if (Util.isInternetAvailable(mContext)) {
            this.context = mContext;
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());
                                JSONObject response_jsonObject = completeObject.getJSONObject(context.getString(R.string.response));
                                String status = response_jsonObject.getString(context.getString(R.string.Status));
                                if (status.equalsIgnoreCase(context.getString(R.string.succeed))) {
                                    String responseToReturn = response_jsonObject.getString(context.getString(R.string.data));
                                    stringListner.getResponseString(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase(context.getString(R.string.failed))) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getErrorString(response_jsonObject.getString(context.getString(R.string.data)));
                                    Snackbar snackbar = Snackbar.make(cdLayout, mContext.getString(R.string.unable_to_process), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, "Unable to process request.. Please try again later.."/*volleyError.getCause().getMessage().toString()*/, Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, "no_internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    /*public void MyJsonArrayRequestGet(final Context mContext,
                                      final CoordinatorLayout cdLayout,
                                      final String methodUrl,
                                      final String methodName) {

        if (Util.isInternetAvailable(mContext)) {
            this.context = mContext;
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);

            requestQueue = getInstance(mContext);

            JsonArrayRequest arrayreq = new JsonArrayRequest(final_url,

                    new Response.Listener<JSONArray>() {


                        @Override
                        public void onResponse(JSONArray response) {

                            dialog.dismiss();
                            try {
                                JSONObject colorObj = response.getJSONObject(0);

                                JSONArray colorArry = colorObj.getJSONArray("results");

                                for (int i = 0; i < colorArry.length(); i++) {

                                    JSONObject jsonObject = colorArry.getJSONObject(i);
                                    JSONArray responseToReturn = jsonObject.getJSONArray(context.getString(R.string.data));
                                    listner.getResponse(responseToReturn, methodName);


                                }

                            } catch (JSONException e) {
                                // If an error occurs, this prints the error to the log
                                e.printStackTrace();
                            }
                        }
                    },
                    // The final parameter overrides the method onErrorResponse() and passes VolleyError
                    //as a parameter
                    new Response.ErrorListener() {

                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley===",error.toString() );
                            dialog.dismiss();
                        }
                    }
            );
            // Adds the JSON array request "arrayreq" to the request queue
            requestQueue.addToRequestQueue(arrayreq);

        }
    }*/
}





