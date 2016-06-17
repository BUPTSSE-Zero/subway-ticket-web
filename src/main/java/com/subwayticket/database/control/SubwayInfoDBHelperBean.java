package com.subwayticket.database.control;

import com.subwayticket.database.model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */

@Stateless
public class SubwayInfoDBHelperBean extends EntityManagerHelper{
    @PersistenceContext(unitName = "SubwayTicketDBPU")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<SubwayLine> getSubwayLineList(int cityID){
        return getSubwayLineList(new City(cityID));
    }

    public List<SubwayLine> getSubwayLineList(City city){
        Query q = entityManager.createQuery("select s from SubwayLine s where s.city = :city order by s.subwayLineId", SubwayLine.class);
        q.setParameter("city", city);
        return q.getResultList();
    }

    public List<SubwayStation> getSubwayStationList(int subwayLineID){
        return getSubwayStationList(new SubwayLine(subwayLineID));
    }

    public List<SubwayStation> getSubwayStationList(SubwayLine subwayLine){
        Query q = entityManager.createQuery("select s from SubwayStation s where s.subwayLine = :subwayLine order by s.subwayStationId");
        q.setParameter("subwayLine", subwayLine);
        return q.getResultList();
    }

    public TicketPrice getTicketPrice(SubwayStation stationA, SubwayStation stationB){
        return getTicketPrice(stationA.getSubwayStationId(), stationB.getSubwayStationId());
    }

    public TicketPrice getTicketPrice(int subwayStationAID, int subwayStationBID){
        TicketPrice ticketPrice = (TicketPrice) find(TicketPrice.class, new TicketPricePK(subwayStationAID, subwayStationBID));
        if(ticketPrice == null){
            ticketPrice = (TicketPrice) find(TicketPrice.class, new TicketPricePK(subwayStationBID, subwayStationAID));
        }
        return ticketPrice;
    }
}
