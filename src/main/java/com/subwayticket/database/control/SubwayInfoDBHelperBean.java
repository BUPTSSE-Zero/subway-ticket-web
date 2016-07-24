package com.subwayticket.database.control;

import com.subwayticket.database.model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */

@Stateless(name = "SubwayInfoDBHelperEJB")
public class SubwayInfoDBHelperBean extends SystemDBHelperBean{
    public List<SubwayLine> getSubwayLineList(int cityID){
        return getSubwayLineList(new City(cityID));
    }

    public List<SubwayLine> getSubwayLineList(City city){
        Query q = getEntityManager().createQuery("select s from SubwayLine s where s.city = :city order by s.subwayLineId", SubwayLine.class);
        q.setParameter("city", city);
        return q.getResultList();
    }

    public List<SubwayStation> getSubwayStationList(int subwayLineID){
        return getSubwayStationList(new SubwayLine(subwayLineID));
    }

    public List<SubwayStation> getSubwayStationList(SubwayLine subwayLine){
        Query q = getEntityManager().createQuery("select s from SubwayStation s where s.subwayLine = :subwayLine order by s.subwayStationId");
        q.setParameter("subwayLine", subwayLine);
        return q.getResultList();
    }

    public TicketPrice getTicketPrice(SubwayStation startStation, SubwayStation endStation){
        return getTicketPrice(startStation.getSubwayStationId(), endStation.getSubwayStationId());
    }

    public TicketPrice getTicketPrice(int startStationID, int endStationBID){
        TicketPrice ticketPrice = (TicketPrice) find(TicketPrice.class, new TicketPricePK(startStationID, endStationBID));
        if(ticketPrice == null){
            ticketPrice = (TicketPrice) find(TicketPrice.class, new TicketPricePK(endStationBID, startStationID));
            if(ticketPrice == null)
                return null;
        }
        return ticketPrice;
    }
}
