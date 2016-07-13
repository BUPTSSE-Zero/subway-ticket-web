package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-9.
 */

@ManagedBean
@ViewScoped
public class PreferenceBean implements Serializable {
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;
    @EJB
    private SystemDBHelperBean systemDBHelperBean;
    private Account user;
    private DualListModel<City> cities;
    private DualListModel<SubwayStation> subwayStations;
    private List<PreferCity> preferCities;
    private List<PreferRoute> preferRoutes;
    private List<PreferSubwayStation> preferSubwayStations;
    boolean displayPreferCities = true;
    boolean displayPreferSubwayStations = true;
    boolean displayPreferRoutes = true;
    private PreferRoute selectPreferRoute = null;

    private int cityId = 0;
    private int startSubwayLineId = 0;
    private int endSubwayLineId = 0;
    private int startSubwayStationId = 0;
    private int endSubwayStationId = 0;
    private List<City> cityList;
    private List<SubwayLine> subwayLineList = new ArrayList<>();
    private List<SubwayStation> startSubwayStationList = new ArrayList<>();
    private List<SubwayStation> endSubwayStationList = new ArrayList<>();
    private boolean canSubmit = false;

    @PostConstruct
    public void init(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        user = (Account) systemDBHelperBean.find(Account.class, user.getPhoneNumber());
        preferCities = user.getPreferCityList();
        preferRoutes = user.getPreferRouteList();
        preferSubwayStations = user.getPreferSubwayStationList();
        List<City> citiesSource = systemDBHelperBean.findAll(City.class);
        List<City> citiesTarget = new ArrayList<>();
        for(PreferCity preferCity : preferCities){
            citiesTarget.add(preferCity.getCity());
            citiesSource.remove(preferCity.getCity());
        }
        cities = new DualListModel<>(citiesSource, citiesTarget);
        List<SubwayStation> subwayStationSource = systemDBHelperBean.findAll(SubwayStation.class);
        List<SubwayStation> subwayStationTarget = new ArrayList<>();
        for(PreferSubwayStation preferSubwayStation : preferSubwayStations){
            subwayStationSource.remove(preferSubwayStation.getSubwayStation());
            subwayStationTarget.add(preferSubwayStation.getSubwayStation());
        }
        subwayStations = new DualListModel<>(subwayStationSource, subwayStationTarget);
        cityList = systemDBHelperBean.findAll(City.class);
    }

    public List<PreferCity> getPreferCities() {
        return preferCities;
    }

    public List<PreferRoute> getPreferRoutes() {
        return preferRoutes;
    }

    public void setPreferRoutes(List<PreferRoute> preferRoutes) {
        this.preferRoutes = preferRoutes;
    }

    public List<PreferSubwayStation> getPreferSubwayStations() {
        return preferSubwayStations;
    }

    public DualListModel<City> getCities() {
        return cities;
    }

    public void setCities(DualListModel<City> cities) {
        this.cities = cities;
    }

    public DualListModel<SubwayStation> getSubwayStations() {
        return subwayStations;
    }

    public void setSubwayStations(DualListModel<SubwayStation> subwayStations) {
        this.subwayStations = subwayStations;
    }

    public boolean isDisplayPreferCities() {
        return displayPreferCities;
    }

    public boolean isDisplayPreferSubwayStations() {
        return displayPreferSubwayStations;
    }

    public boolean isDisplayPreferRoutes() {
        return displayPreferRoutes;
    }

    public void onChangePreferCitiesClicked(){
            displayPreferCities = false;
    }

    public void onChangePreferSubwayStationsClicked(){
        displayPreferSubwayStations = false;
    }

    public void onChangePreferRoutesClicked(){
        displayPreferRoutes = false;
    }

    public void onConfirmPreferCitiesChange(){
        displayPreferCities = true;
    }

    public void onConfirmPreferSubwayStationsChange(){
        displayPreferSubwayStations = true;
    }

    public void onConfirmPreferRoutesChange(){
        displayPreferRoutes = true;
    }

    public void onPreferCitiesTransfer(TransferEvent event){
        for(Object item : event.getItems()){
            PreferCity preferCity = new PreferCity(user.getPhoneNumber(), ((City) item).getCityId());
            if(preferCities.contains(preferCity)){
                preferCities.remove(preferCity);
                systemDBHelperBean.remove(preferCity);
            }
            else{
                systemDBHelperBean.create(preferCity);
                systemDBHelperBean.refresh(preferCity);
            }
        }
    }

    public void onPreferSubwayStationsTransfer(TransferEvent event){
        for(Object item : event.getItems()){
            PreferSubwayStation preferSubwayStation = new PreferSubwayStation(user.getPhoneNumber(), ((SubwayStation) item).getSubwayStationId());
            if(preferSubwayStations.contains(preferSubwayStation)){
                preferSubwayStations.remove(preferSubwayStation);
                systemDBHelperBean.remove(preferSubwayStation);
            }
            else{
                systemDBHelperBean.create(preferSubwayStation);
                systemDBHelperBean.refresh(preferSubwayStation);
            }
        }
    }

    public List<City> getCityList() {
        return cityList;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStartSubwayLineId() {
        return startSubwayLineId;
    }

    public void setStartSubwayLineId(int startSubwayLineId) {
        this.startSubwayLineId = startSubwayLineId;
    }

    public int getEndSubwayLineId() {
        return endSubwayLineId;
    }

    public void setEndSubwayLineId(int endSubwayLineId) {
        this.endSubwayLineId = endSubwayLineId;
    }

    public int getEndSubwayStationId() {
        return endSubwayStationId;
    }

    public void setEndSubwayStationId(int endSubwayStationId) {
        this.endSubwayStationId = endSubwayStationId;
    }

    public int getStartSubwayStationId() {
        return startSubwayStationId;
    }

    public void setStartSubwayStationId(int startSubwayStationId) {
        this.startSubwayStationId = startSubwayStationId;
    }

    public boolean isCanSubmit() {
        return canSubmit;
    }

    public List<SubwayLine> getSubwayLineList() {
        return subwayLineList;
    }

    public List<SubwayStation> getStartSubwayStationList() {
        return startSubwayStationList;
    }

    public List<SubwayStation> getEndSubwayStationList() {
        return endSubwayStationList;
    }

    public void onCityIdChange(){
        canSubmit = false;
        subwayLineList = new ArrayList<>();
        startSubwayLineId = 0;
        startSubwayStationId = 0;
        endSubwayStationId = 0;
        endSubwayLineId = 0;
        if(cityId != 0)
            subwayLineList = subwayInfoDBHelperBean.getSubwayLineList(cityId);
    }

    public void onStartSubwayLineIdChange(){
        canSubmit = false;
        startSubwayStationList = new ArrayList<>();
        startSubwayStationId = 0;
        if(startSubwayLineId != 0)
            startSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(startSubwayLineId);
    }

    public void onEndSubwayLineIdChange(){
        canSubmit = false;
        endSubwayStationList = new ArrayList<>();
        endSubwayStationId = 0;
        if(endSubwayLineId != 0)
            endSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(endSubwayLineId);
    }

    public void onSubwayStationIdChange(){
        if(endSubwayStationId!=0 && startSubwayStationId!=0 && endSubwayStationId!=startSubwayStationId){
            canSubmit = true;
            return;
        }
        canSubmit = false;
    }

    public void onAddClicked(){
        PreferRoute preferRoute = new PreferRoute(user.getPhoneNumber(), startSubwayStationId, endSubwayStationId);
        if(!preferRoutes.contains(preferRoute)) {
            systemDBHelperBean.create(preferRoute);
            systemDBHelperBean.refresh(preferRoute);
            preferRoutes.add(preferRoute);
        }
    }

    public void onSelect(SelectEvent event){
        selectPreferRoute = new PreferRoute((PreferRoute)event.getObject());
    }

    public void onDeleteClicked(){
        if(selectPreferRoute != null){
            PreferRoute preferRoute = new PreferRoute(selectPreferRoute);
            systemDBHelperBean.remove(preferRoute);
            preferRoutes.remove(preferRoute);
        }
    }
}
