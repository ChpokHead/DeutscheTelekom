package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.WaypointDao;
import com.chpok.logiweb.dto.WaypointDto;
import com.chpok.logiweb.dto.WaypointsPair;
import com.chpok.logiweb.exception.InvalidEntityException;
import com.chpok.logiweb.model.Order;
import com.chpok.logiweb.model.enums.CargoStatus;
import com.chpok.logiweb.model.enums.WaypointType;
import com.chpok.logiweb.service.CargoService;
import com.chpok.logiweb.service.WaypointService;
import com.chpok.logiweb.mapper.impl.WaypointMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class WaypointServiceImpl implements WaypointService {
    private static final Logger LOGGER = LogManager.getLogger(WaypointServiceImpl.class);

    private final WaypointDao waypointDao;
    private final WaypointMapper waypointMapper;

    @Autowired
    private CargoService cargoService;

    public WaypointServiceImpl(WaypointDao waypointDao, WaypointMapper waypointMapper) {
        this.waypointDao = waypointDao;
        this.waypointMapper = waypointMapper;
    }

    @Override
    public void saveWaypoint(WaypointDto waypoint) {
        try {
            waypointDao.save(waypointMapper.mapDtoToEntity(waypoint));

            final String info = String.format("new waypoint with location = %s was created", waypoint.getLocation().getName());

            LOGGER.info(info);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("saving waypoint exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void deleteWaypoint(Long id) {
        try {
            waypointDao.deleteById(id);

            final String info = String.format("waypoint with id = %d was deleted",id);

            LOGGER.info(info);
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("deleting waypoint by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateWaypoint(WaypointDto waypoint) {
        try {
            waypointDao.update(waypointMapper.mapDtoToEntity(waypoint));
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating waypoint exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public List<WaypointDto> getAllWaypointsByOrderId(Long id) {
        try {
            if (id != null) {
                return waypointDao.findAllByOrderId(id).stream().map(waypointMapper::mapEntityToDto).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all waypoints by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<WaypointsPair> getAllWaypointsPairByOrderId(Long id) {
        try {
            final List<WaypointDto> waypoints = getAllWaypointsByOrderId(id);
            final List<WaypointsPair> waypointsPairs = new ArrayList<>();

            for (int i = 0; i < waypoints.size(); i+=2) {
                final WaypointsPair pair = new WaypointsPair();

                pair.getPair().add(waypoints.get(i));
                pair.getPair().add(waypoints.get(i + 1));

                waypointsPairs.add(pair);
            }

            return waypointsPairs;
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all waypoints pair by order id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public void updateWaypointsStatus(List<Order.Waypoint> waypoints) {
        try {
            final List<Order.Waypoint> updatedWaypoints = new ArrayList<>();

            for (Order.Waypoint waypoint : waypoints) {
                final Order.Waypoint updatingWaypoint
                        = waypointDao.findById(waypoint.getId()).orElseThrow(NoSuchElementException::new);

                updatingWaypoint.setIsDone(waypoint.getIsDone());

                if (Boolean.TRUE.equals(updatingWaypoint.getIsDone())) {
                    updateWaypointCargoStatus(updatingWaypoint);
                }

                updatedWaypoints.add(updatingWaypoint);
            }

            updateWaypoints(updatedWaypoints);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating waypoints status exception");

            throw new InvalidEntityException();
        }
    }

    private void updateWaypointCargoStatus(Order.Waypoint waypoint) {
        try {
            if (waypoint.getType().equals(WaypointType.LOADING)) {
                cargoService.updateCargoStatus(waypoint.getCargo().getId(), CargoStatus.SHIPPED);
            } else {
                cargoService.updateCargoStatus(waypoint.getCargo().getId(), CargoStatus.DELIVERED);
            }
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating waypoint cargo status exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public void updateWaypoints(List<Order.Waypoint> waypoints) {
        try {
            for (Order.Waypoint waypoint : waypoints) {
                waypointDao.update(waypoint);
            }
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException e) {
            LOGGER.error("updating waypoints exception");

            throw new InvalidEntityException();
        }
    }

    @Override
    public boolean checkAllWaypointsComplete(List<Order.Waypoint> waypoints) {
        for (Order.Waypoint waypoint : waypoints) {
            if (Boolean.FALSE.equals(waypoint.getIsDone())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<WaypointDto> getAllLoadingWaypointsByOrderId(Long orderId) {
        try {
            if (orderId != null) {
                List<Order.Waypoint> orderWaypoints = waypointDao.findAllByOrderId(orderId);

                if (orderWaypoints.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    return orderWaypoints.stream()
                            .filter(waypoint -> waypoint.getType() == WaypointType.LOADING)
                            .map(waypointMapper::mapEntityToDto).collect(Collectors.toList());
                }
            } else {
                return Collections.emptyList();
            }
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting all loading waypoints by order id exception");

            throw new EntityNotFoundException();
        }
    }

}
