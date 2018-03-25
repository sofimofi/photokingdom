package ca.senecacollege.prj666.photokingdom.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.models.City;
import ca.senecacollege.prj666.photokingdom.models.Continent;
import ca.senecacollege.prj666.photokingdom.models.Country;
import ca.senecacollege.prj666.photokingdom.models.Province;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Dialog to select a city
 */
public class CityDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "CityDialogFragment";

    // Service for Retrofit
    PhotoKingdomService service;

    // Model lists
    private List<Continent> mContinents;
    private List<Country> mCountries;
    private List<Province> mProvinces;
    private List<City> mCities;

    // Spinners
    private Spinner mSpinnerContinent;
    private Spinner mSpinnerCountry;
    private Spinner mSpinnerProvince;
    private Spinner mSpinnerCity;

    // Adapters for spinners
    private ArrayAdapter<String> mContinentAdapter;
    private ArrayAdapter<String> mCountryAdapter;
    private ArrayAdapter<String> mProvinceAdapter;
    private ArrayAdapter<String> mCityAdapter;

    // Selected city to pass ResigterActivity
    City selectedCity;

    // Listener to communicate to RegisterActivity
    public interface OnCitySelectedListener {
        public void onCitySelected(City city);
    }
    OnCitySelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCitySelectedListener) {
            mListener = (OnCitySelectedListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a retrofit service for PhotoKingdomAPI
        service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // View inflation
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_city, null);

        // Build a dialog with "OK" button
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(R.string.city)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedCity != null) {
                            mListener.onCitySelected(selectedCity);
                        }
                    }
                });

        // Initialize spinners
        initSpinners(view);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (service != null) {
            // Get continents by PhotoKingdomAPI
            getContinents();
        }
    }

    /**
     * Call PhotoKingdomAPI to get all continents
     */
    private void getContinents() {
        Call<List<Continent>> call = service.getContinents();
        call.enqueue(new Callback<List<Continent>>() {
            @Override
            public void onResponse(Call<List<Continent>> call, Response<List<Continent>> response) {
                if (response.isSuccessful()) {
                    mContinents = response.body();
                    setContinents();
                } else {
                    try {
                        Log.d(TAG, "[getContinents] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Continent>> call, Throwable t) {
                Log.d(TAG, "[getContinents] " + t.getMessage());
            }
        });
    }

    /**
     * Set retrieved continents to a spinner
     */
    private void setContinents() {
        if (mContinents != null) {
            for (int i = 0; i < mContinents.size(); i++) {
                mContinentAdapter.add(mContinents.get(i).getName());
            }

            mSpinnerContinent.setEnabled(true);
        }
    }

    /**
     * Call PhotoKingdomAPI to get all countries by continent id
     */
    private void getCountries(int continentId) {
        Call<Continent> call = service.getContinentWithCountries(continentId);
        call.enqueue(new Callback<Continent>() {
            @Override
            public void onResponse(Call<Continent> call, Response<Continent> response) {
                if (response.isSuccessful()) {
                    mCountries = response.body().getCountries();
                    setCountries();
                } else {
                    try {
                        Log.d(TAG, "[getCountries] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Continent> call, Throwable t) {
                Log.d(TAG, "[getCountries] " + t.getMessage());
            }
        });
    }

    /**
     * Set retrieved countries to a spinner
     */
    private void setCountries() {
        mCountryAdapter.clear();
        mCountryAdapter.add(getString(R.string.msg_choose_country));

        if (mCountries != null) {
            for (int i = 0; i < mCountries.size(); i++) {
                mCountryAdapter.add(mCountries.get(i).getName());
            }

            mSpinnerCountry.setEnabled(true);
        }
    }

    /**
     * Call PhotoKingdomAPI to get all provinces by country id
     */
    private void getProvinces(int countryId) {
        Call<Country> call = service.getCountryWithProvinces(countryId);
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                if (response.isSuccessful()) {
                    mProvinces = response.body().getProvinces();
                    setProvinces();
                } else {
                    try {
                        Log.d(TAG, "[getProvinces] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.d(TAG, "[getProvinces] " + t.getMessage());
            }
        });
    }

    /**
     * Set retrieved provinces to a spinner
     */
    private void setProvinces() {
        mProvinceAdapter.clear();
        mProvinceAdapter.add(getString(R.string.msg_choose_province));

        if (mProvinces != null) {
            for (int i = 0; i < mProvinces.size(); i++) {
                mProvinceAdapter.add(mProvinces.get(i).getName());
            }

            mSpinnerProvince.setEnabled(true);
        }
    }

    /**
     * Call PhotoKingdomAPI to get all cities by province id
     */
    private void getCities(int provinceId) {
        Call<Province> call = service.getProvinceWithCities(provinceId);
        call.enqueue(new Callback<Province>() {
            @Override
            public void onResponse(Call<Province> call, Response<Province> response) {
                if (response.isSuccessful()) {
                    mCities = response.body().getCities();
                    setCities();
                } else {
                    try {
                        Log.d(TAG, "[getCities] " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                Log.d(TAG, "[getCities] " + t.getMessage());
            }
        });
    }

    /**
     * Set retrieved cities to a spinner
     */
    private void setCities() {
        mCityAdapter.clear();
        mCityAdapter.add(getString(R.string.msg_choose_city));

        if (mCities != null) {
            for (int i = 0; i < mCities.size(); i++) {
                mCityAdapter.add(mCities.get(i).getName());
            }

            mSpinnerCity.setEnabled(true);
        }
    }

    /**
     * Initialize all spinners and its adapters
     * @param view
     */
    private void initSpinners(View view) {
        // Continent
        mSpinnerContinent = (Spinner)view.findViewById(R.id.spinnerContinent);
        mSpinnerContinent.setOnItemSelectedListener(this);
        mSpinnerContinent.setEnabled(false);

        List<String> continentEntries = new ArrayList<String>();
        continentEntries.add(getString(R.string.msg_choose_continent));
        mContinentAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, continentEntries);

        mSpinnerContinent.setAdapter(mContinentAdapter);

        // Country
        mSpinnerCountry = (Spinner)view.findViewById(R.id.spinnerCountry);
        mSpinnerCountry.setOnItemSelectedListener(this);

        List<String> countryEntries = new ArrayList<String>();
        countryEntries.add(getString(R.string.msg_choose_country));
        mCountryAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,  countryEntries);

        mSpinnerCountry.setAdapter(mCountryAdapter);
        mSpinnerCountry.setEnabled(false);

        // Province
        mSpinnerProvince = (Spinner)view.findViewById(R.id.spinnerProvince);
        mSpinnerProvince.setOnItemSelectedListener(this);

        List<String> provinceEntries = new ArrayList<String>();
        provinceEntries.add(getString(R.string.msg_choose_province));
        mProvinceAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,  provinceEntries);

        mSpinnerProvince.setAdapter(mProvinceAdapter);
        mSpinnerProvince.setEnabled(false);

        // City
        mSpinnerCity = (Spinner)view.findViewById(R.id.spinnerCity);
        mSpinnerCity.setOnItemSelectedListener(this);

        List<String> cityEntries = new ArrayList<String>();
        cityEntries.add(getString(R.string.msg_choose_city));
        mCityAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,  cityEntries);
        mSpinnerCity.setAdapter(mCityAdapter);
        mSpinnerCity.setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            switch (adapterView.getId()) {
                case R.id.spinnerContinent:
                    getCountries(mContinents.get(i - 1).getId());
                    mSpinnerCountry.setEnabled(true);
                    break;
                case R.id.spinnerCountry:
                    getProvinces(mCountries.get(i - 1).getId());
                    mSpinnerProvince.setEnabled(true);
                    break;
                case R.id.spinnerProvince:
                    getCities(mProvinces.get(i - 1).getId());
                    mSpinnerCity.setEnabled(true);
                    break;
                case R.id.spinnerCity:
                    selectedCity = mCities.get(i - 1);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
