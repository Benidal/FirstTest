public void onMapSearch(View view) {

        EditText locationSearch = (EditText) findViewById(R.id.arrival);
        String location = locationSearch.getText().toString();
        List<Address>addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                Toast.makeText(this,"Destination not found",Toast.LENGTH_SHORT).show();

            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerPoints.add(latLng);
            Toast.makeText(MapsActivity.this,"added position 2 "+MarkerPoints.size(),Toast.LENGTH_SHORT).show();

            mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(location+" ("+address.getLatitude()+","+address.getLongitude()+")").icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            //listClients.add(new Client(R.drawable.flag,"Nasser","client2 requesting cab",100,address.getLatitude(), address.getLongitude()));
            //Toast.makeText(MapsActivity.this,"client attended at "+" ("+address.getLatitude()+","+address.getLongitude()+")",Toast.LENGTH_SHORT).show();



            /* if (MarkerPoints.size() > 2) {
                   // MarkerPoints.clear();
                    mMap.clear();
                }*/


            // Checks, whether start and end locations are captured
            if (MarkerPoints.size() >= 2) {
                LatLng origin = MarkerPoints.get(0);
                LatLng dest = MarkerPoints.get(MarkerPoints.size()-1);

                // Getting URL to the Google Directions API
                String url = getUrl(origin, dest);
                Log.d("onMapClick", url.toString());
                FetchUrl FetchUrl = new FetchUrl();

                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);
                //move map camera
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                build_retrofit_and_get_response("driving");

                final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
                if (mapView.getViewTreeObserver().isAlive()) {
                    mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onGlobalLayout() {
                            LatLngBounds.Builder bld = new LatLngBounds.Builder();
                            for (int i = 0; i < MarkerPoints.size(); i++) {
                                LatLng ll = new LatLng(MarkerPoints.get(i).latitude, MarkerPoints.get(i).longitude);
                                bld.include(ll);
                            }
                            LatLngBounds bounds = bld.build();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
                            mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        }
                    });
                }
            }

        }
    }