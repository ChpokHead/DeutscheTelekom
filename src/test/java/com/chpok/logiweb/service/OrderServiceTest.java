package com.chpok.logiweb.service;

/*
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class OrderServiceTest {
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private TruckMapper truckMapper;
    @Mock
    private OrderDao orderDao;
    @Mock
    private DriverService driverService;
    @Mock
    private TruckService truckService;
    @Mock
    private WaypointService waypointService;
    @Mock
    private CargoService cargoService;
    @Mock
    private LocationMapService locationMapService;
    @Mock
    private KafkaTemplate<String, LogiwebMessage> kafkaTemplate;
    @Autowired
    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllOrdersShouldReturnCorrectOrderDtoList() {
        final Order firstOrder = Order.builder()
                .withId(1L).build();

        final Order secondOrder = Order.builder()
                .withId(2L).build();

        final OrderDto firstOrderDto = new OrderDto();

        firstOrderDto.setId(1L);

        final OrderDto secondOrderDto = new OrderDto();

        secondOrderDto.setId(2L);

        final List<OrderDto> expected =
                Arrays.asList(firstOrderDto, secondOrderDto);

        when(orderMapper.mapEntityToDto(firstOrder)).thenReturn(firstOrderDto);
        when(orderMapper.mapEntityToDto(secondOrder)).thenReturn(secondOrderDto);
        when(orderDao.findAll()).thenReturn(Arrays.asList(firstOrder, secondOrder));

        final List<OrderDto> actual = orderService.getAllOrders();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveOrderShouldCorrectlySaveOrder() {
        final Order savingOrder = Order.builder()
                .withId(1L)
                .withStatus(true)
                .build();

        final OrderDto savingOrderDto = new OrderDto();

        savingOrderDto.setId(1L);
        savingOrderDto.setIsCompleted(true);

        when(orderMapper.mapDtoToEntity(savingOrderDto)).thenReturn(savingOrder);

        when(orderDao.save(savingOrder)).thenReturn(savingOrder.getId());

        assertThatNoException().isThrownBy(() -> orderService.saveOrder(savingOrderDto));
    }

    @Test
    void updateOrderShouldCorrectlyUpdateOrder() {
        final Order updatedOrder = Order.builder()
                .withId(1L)
                .withStatus(true)
                .build();

        final OrderDto updatedOrderDto = new OrderDto();

        updatedOrderDto.setId(1L);
        updatedOrderDto.setIsCompleted(true);

        when(orderMapper.mapDtoToEntity(updatedOrderDto)).thenReturn(updatedOrder);

        doNothing().when(orderDao).update(updatedOrder);

        assertThatNoException().isThrownBy(() -> orderService.updateOrder(updatedOrderDto));
    }

    @Test
    void updateOrderCurrentTruckShouldUpdateOrderCurrentTruck() {
        final Long updatingOrderId = 2L;
        final Truck expected = Truck.builder().withId(2L).withRegNumber("PO43623").build();
        final Order oldOrder = Order.builder()
                .withId(updatingOrderId)
                .withTruck(Truck.builder().withId(1L).withRegNumber("FG20343").build())
                .build();

        final OrderDto oldOrderDto = new OrderDto();

        oldOrderDto.setId(updatingOrderId);
        oldOrderDto.setCurrentTruck(oldOrder.getCurrentTruck());

        final TruckDto updatingTruck = new TruckDto();

        updatingTruck.setId(2L);
        updatingTruck.setRegNumber("PO43623");

        when(orderDao.findById(updatingOrderId)).thenReturn(Optional.of(oldOrder));
        when(orderMapper.mapEntityToDto(oldOrder)).thenReturn(oldOrderDto);
        when(truckMapper.mapDtoToEntity(updatingTruck)).thenReturn(expected);
        doNothing().when(truckService).updateTruckCurrentOrder(updatingTruck.getId(), oldOrderDto);

        orderService.updateOrderCurrentTruck(oldOrder.getId(), updatingTruck);

        Mockito.verify(truckService).updateTruckCurrentOrder(updatingTruck.getId(), oldOrderDto);

        final Truck actual = oldOrderDto.getCurrentTruck();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateOrderStatusShouldProperlyUpdateOrderStatus() {
        final Long updatingOrderId = 2L;
        final Order updatedOrder = Order.builder()
                .withId(updatingOrderId)
                .withStatus(OrderStatus.COMPLETED)
                .build();
        final OrderStatus expected = OrderStatus.CLOSED;

        when(orderDao.findById(updatingOrderId)).thenReturn(Optional.ofNullable(updatedOrder));
        when(orderMapper.mapEntityToDto(oldOrder)).thenReturn(oldOrderDto);

        when(orderMapper.mapDtoToEntity(oldOrderDto)).thenReturn(oldOrder);
        doNothing().when(orderDao).update(oldOrder);

        orderService.updateOrderStatus(updatedOrder);

        final Boolean actual = oldOrderDto.getIsCompleted();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateOrderStartAndEndDatesShouldProperlyUpdateOrderStartAndAndDatesByOrderId() {
        final Long updatingOrderId = 2L;
        final LocalDate updatedStartDate = LocalDate.of(2021, 6, 12);
        final LocalDate updatedEndDate = LocalDate.of(2021, 6, 20);
        final Order oldOrder = Order.builder()
                .withId(updatingOrderId)
                .withStatus(false)
                .build();
        final OrderDto oldOrderDto = new OrderDto();

        oldOrderDto.setId(updatingOrderId);
        oldOrderDto.setIsCompleted(false);

        when(orderDao.findById(updatingOrderId)).thenReturn(Optional.ofNullable(oldOrder));
        when(orderMapper.mapEntityToDto(oldOrder)).thenReturn(oldOrderDto);

        when(orderMapper.mapDtoToEntity(oldOrderDto)).thenReturn(oldOrder);
        doNothing().when(orderDao).update(oldOrder);

        orderService.updateOrderStartAndEndDates(updatingOrderId, updatedStartDate, updatedEndDate);

        assertThat(oldOrderDto.getStartDate()).isEqualTo(updatedStartDate);
        assertThat(oldOrderDto.getEndDate()).isEqualTo(updatedEndDate);
    }

    @Test
    void deleteOrderStartAndEndDatesWithDriversShouldSetOrderStartAndEndDatesToNull() {
        final Long updatingOrderId = 2L;
        final Order oldOrder = Order.builder()
                .withId(updatingOrderId)
                .withStatus(false)
                .withStartDate(LocalDate.of(2021, 6, 12))
                .withEndDate(LocalDate.of(2021, 6, 20))
                .withDrivers(Collections.emptyList())
                .build();
        final OrderDto oldOrderDto = new OrderDto();

        oldOrderDto.setId(updatingOrderId);
        oldOrderDto.setIsCompleted(false);
        oldOrderDto.setCurrentDrivers(Collections.emptyList());
        oldOrderDto.setStartDate(LocalDate.of(2021, 6, 12));
        oldOrderDto.setEndDate(LocalDate.of(2021, 6, 20));

        when(orderDao.findById(updatingOrderId)).thenReturn(Optional.ofNullable(oldOrder));
        when(orderMapper.mapEntityToDto(oldOrder)).thenReturn(oldOrderDto);

        when(orderMapper.mapDtoToEntity(oldOrderDto)).thenReturn(oldOrder);
        doNothing().when(orderDao).update(oldOrder);

        orderService.deleteOrderStartAndEndDatesWithDrivers(updatingOrderId);

        assertThat(oldOrderDto.getStartDate()).isNull();
        assertThat(oldOrderDto.getEndDate()).isNull();
    }

    @Test
    void deleteOrderStartAndEndDatesWithDriversShouldSetOrderStartAndEndDatesToNullAndDeleteDriversIfCurrentDriversNotEmpty() {
        final Long updatingOrderId = 2L;
        final Driver firstOrderDriver = Driver.builder()
                .withId(1L)
                .withFirstName("Иван")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)28)
                .withStatus(DriverStatus.SHIFTING).build();
        final Driver secondOrderDriver = Driver.builder()
                .withId(2L)
                .withFirstName("Андрей")
                .withLastName("Иванов")
                .withMonthWorkedHours((short)6)
                .withStatus(DriverStatus.DRIVING).build();
        final Order oldOrder = Order.builder()
                .withId(updatingOrderId)
                .withStatus(false)
                .withStartDate(LocalDate.of(2021, 6, 12))
                .withEndDate(LocalDate.of(2021, 6, 20))
                .withDrivers(Arrays.asList(firstOrderDriver, secondOrderDriver))
                .build();
        final OrderDto oldOrderDto = new OrderDto();

        oldOrderDto.setId(updatingOrderId);
        oldOrderDto.setIsCompleted(false);
        oldOrderDto.setCurrentDrivers(Arrays.asList(firstOrderDriver, secondOrderDriver));
        oldOrderDto.setStartDate(LocalDate.of(2021, 6, 12));
        oldOrderDto.setEndDate(LocalDate.of(2021, 6, 20));

        when(orderDao.findById(updatingOrderId)).thenReturn(Optional.ofNullable(oldOrder));
        when(orderMapper.mapEntityToDto(oldOrder)).thenReturn(oldOrderDto);

        doNothing().when(driverService).updateDriverWhenOrderIsDeleted(firstOrderDriver.getId());
        doNothing().when(driverService).updateDriverWhenOrderIsDeleted(secondOrderDriver.getId());

        when(orderMapper.mapDtoToEntity(oldOrderDto)).thenReturn(oldOrder);
        doNothing().when(orderDao).update(oldOrder);

        orderService.deleteOrderStartAndEndDatesWithDrivers(updatingOrderId);

        Mockito.verify(driverService).updateDriverWhenOrderIsDeleted(firstOrderDriver.getId());
        Mockito.verify(driverService).updateDriverWhenOrderIsDeleted(secondOrderDriver.getId());

        assertThat(oldOrderDto.getStartDate()).isNull();
        assertThat(oldOrderDto.getEndDate()).isNull();
    }

    @Test
    void getOrderByIdShouldReturnCorrectOrder() {
        final Long foundOrderId = 1L;
        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .build();

        final OrderDto expected = new OrderDto();

        expected.setId(foundOrderId);
        expected.setIsCompleted(false);

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));

        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(expected);

        final OrderDto actual = orderService.getOrderById(foundOrderId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkOrderIsCompletedShouldReturnTrueIfOrderIsNotNullAndOrderWaypointsAreNotNullAndOrderWaypointsCompleted() {
        final Long foundOrderId = 1L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .withIsDone(true)
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withIsDone(true)
                .build();
        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);
        when(waypointService.checkAllWaypointsComplete(foundOrderDto.getWaypoints())).thenReturn(true);

        assertThat(orderService.checkOrderIsCompleted(foundOrderId)).isTrue();
    }

    @Test
    void checkOrderIsCompletedShouldThrowEntityNotFoundExceptionIfOrderIsNull(){
        final Long foundOrderId = 1L;

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> orderService.checkOrderIsCompleted(foundOrderId));
    }

    @Test
    void checkOrderIsCompletedShouldReturnFalseIfOrderWaypointsIsNull(){
        final Long foundOrderId = 1L;
        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .build();
        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        assertThat(orderService.checkOrderIsCompleted(foundOrderId)).isFalse();
    }

    @Test
    void checkOrderIsCompletedShouldReturnFalseIfOrderWaypointsAreNotComplete(){
        final Long foundOrderId = 1L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .withIsDone(true)
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withIsDone(false)
                .build();
        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);
        when(waypointService.checkAllWaypointsComplete(foundOrderDto.getWaypoints())).thenReturn(false);

        assertThat(orderService.checkOrderIsCompleted(foundOrderId)).isFalse();
    }

    @Test
    void getSuitableTrucksForOrderShouldReturnEmptyListIfOrderWaypointsIsEmpty() {
        final Long foundOrderId = 1L;
        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Collections.emptyList())
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Collections.emptyList());

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        assertThat(orderService.getSuitableTrucksForOrder(foundOrderId)).isEmpty();
    }

    @Test
    void getSuitableTrucksForOrderShouldReturnCorrectTruckList() {
        final Long foundOrderId = 1L;
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .withIsDone(true)
                .withLocation(new Location(1L, "Москва"))
                .withCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(10000).build())
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.LOADING)
                .withLocation(new Location(1L, "Москва"))
                .withIsDone(false)
                .build();

        final WaypointDto firstWaypointDto = new WaypointDto();

        firstWaypointDto.setId(firstWaypoint.getId());
        firstWaypointDto.setType(firstWaypoint.getType().ordinal());
        firstWaypointDto.setIsDone(firstWaypoint.getIsDone());
        firstWaypointDto.setCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(10000).build());
        firstWaypointDto.setLocation(new Location(1L, "Москва"));

        final WaypointDto secondWaypointDto = new WaypointDto();

        secondWaypointDto.setId(secondWaypoint.getId());
        secondWaypointDto.setType(secondWaypoint.getType().ordinal());
        secondWaypointDto.setIsDone(secondWaypoint.getIsDone());
        secondWaypointDto.setLocation(new Location(1L, "Москва"));

        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .build();
        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));

        final TruckDto firstTruckDto = new TruckDto();

        firstTruckDto.setId(1L);
        firstTruckDto.setCapacity((short)12);
        firstTruckDto.setLocation(new Location(1L, "Москва"));
        firstTruckDto.setStatus(TruckStatus.OK.ordinal());

        final TruckDto secondTruckDto = new TruckDto();

        secondTruckDto.setId(2L);
        secondTruckDto.setCapacity((short)1);
        secondTruckDto.setLocation(new Location(1L, "Москва"));
        secondTruckDto.setStatus(TruckStatus.OK.ordinal());

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        when(truckService.getTrucksWithOKStatusAndWithoutCurrentOrder()).thenReturn(Arrays.asList(firstTruckDto, secondTruckDto));
        when(waypointService.getAllLoadingWaypointsByOrderId(foundOrderId)).thenReturn(Collections.singletonList(firstWaypointDto));

        final List<TruckDto> expected = Collections.singletonList(firstTruckDto);
        final List<TruckDto> actual = orderService.getSuitableTrucksForOrder(foundOrderId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getOrderDistanceShouldReturnCorrectDistance() {
        final Long orderId = 1L;
        final WaypointDto firstWaypointDto = new WaypointDto();
        final short expected = 706;

        firstWaypointDto.setId(1L);
        firstWaypointDto.setType(WaypointType.LOADING.ordinal());
        firstWaypointDto.setIsDone(false);
        firstWaypointDto.setLocation(new Location(8L, "Москва"));

        final WaypointDto secondWaypointDto = new WaypointDto();

        secondWaypointDto.setId(2L);
        secondWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        secondWaypointDto.setIsDone(false);
        secondWaypointDto.setLocation(new Location(9L, "Санкт-Петербург"));

        final WaypointsPair waypointsPair = new WaypointsPair();
        waypointsPair.setPair(Arrays.asList(firstWaypointDto, secondWaypointDto));

        when(waypointService.getAllWaypointsPairByOrderId(orderId)).thenReturn(Collections.singletonList(waypointsPair));
        when(locationMapService.getDistanceBetweenLocationsByIds(firstWaypointDto.getLocation().getId(),
                secondWaypointDto.getLocation().getId())).thenReturn(expected);

        assertThat(orderService.getOrderDistance(orderId)).isEqualTo(expected);
    }

    @Test
    void getOrderTravelHoursShouldReturnCorrectOrderTravelHours() {
        final Long orderId = 1L;
        final WaypointDto firstWaypointDto = new WaypointDto();
        final short orderDistance = 706;

        firstWaypointDto.setId(1L);
        firstWaypointDto.setType(WaypointType.LOADING.ordinal());
        firstWaypointDto.setIsDone(false);
        firstWaypointDto.setLocation(new Location(8L, "Москва"));

        final WaypointDto secondWaypointDto = new WaypointDto();

        secondWaypointDto.setId(2L);
        secondWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        secondWaypointDto.setIsDone(false);
        secondWaypointDto.setLocation(new Location(9L, "Санкт-Петербург"));

        final WaypointsPair waypointsPair = new WaypointsPair();
        waypointsPair.setPair(Arrays.asList(firstWaypointDto, secondWaypointDto));

        when(waypointService.getAllWaypointsPairByOrderId(orderId)).thenReturn(Collections.singletonList(waypointsPair));
        when(locationMapService.getDistanceBetweenLocationsByIds(firstWaypointDto.getLocation().getId(),
                secondWaypointDto.getLocation().getId())).thenReturn(orderDistance);

        final short actual = orderService.getOrderTravelHours(orderId);
        final short expected = (short) Math.round((double) orderDistance / Truck.TRUCK_AVERAGE_SPEED_IN_KMH);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getSuitableDriversForOrderShouldReturnCorrectDriverListIfOrderWaypointsNotEmpty() {
        final Long foundOrderId = 1L;
        final short orderDistance = 706;
        final DriverDto firstDriverDto = new DriverDto();

        firstDriverDto.setPersonalNumber(1L);
        firstDriverDto.setLocation(new Location(8L, "Москва"));
        firstDriverDto.setMonthWorkedHours((short)12);

        final DriverDto secondDriverDto = new DriverDto();

        secondDriverDto.setPersonalNumber(2L);
        secondDriverDto.setLocation(new Location(12L, "Омск"));
        secondDriverDto.setMonthWorkedHours((short)100);

        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .withIsDone(false)
                .withLocation(new Location(8L, "Москва"))
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withLocation(new Location(9L, "Санкт-Петербург"))
                .withIsDone(false)
                .build();

        final WaypointDto firstWaypointDto = new WaypointDto();

        firstWaypointDto.setId(1L);
        firstWaypointDto.setType(WaypointType.LOADING.ordinal());
        firstWaypointDto.setIsDone(false);
        firstWaypointDto.setLocation(new Location(8L, "Москва"));

        final WaypointDto secondWaypointDto = new WaypointDto();

        secondWaypointDto.setId(2L);
        secondWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        secondWaypointDto.setIsDone(false);
        secondWaypointDto.setLocation(new Location(9L, "Санкт-Петербург"));

        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .withTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                        .withLocation(new Location(8L, "Москва")).build())
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));
        foundOrderDto.setCurrentTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                .withLocation(new Location(8L, "Москва")).build());

        final WaypointsPair waypointsPair = new WaypointsPair();
        waypointsPair.setPair(Arrays.asList(firstWaypointDto, secondWaypointDto));

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        when(waypointService.getAllWaypointsPairByOrderId(foundOrderId)).thenReturn(Collections.singletonList(waypointsPair));
        when(locationMapService.getDistanceBetweenLocationsByIds(firstWaypointDto.getLocation().getId(),
                secondWaypointDto.getLocation().getId())).thenReturn(orderDistance);

        when(driverService.getAllDriversWithoutCurrentOrder()).thenReturn(Arrays.asList(firstDriverDto, secondDriverDto));

        assertThat(orderService.getSuitableDriversForOrder(foundOrderId)).isEqualTo(Collections.singletonList(firstDriverDto));
    }

    @Test
    void getSuitableDriversForOrderShouldReturnEmptyListIfOrderWaypointsAreEmpty() {
        final Long foundOrderId = 1L;

        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withWaypoints(Collections.emptyList())
                .withTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                        .withLocation(new Location(8L, "Москва")).build())
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setWaypoints(Collections.emptyList());
        foundOrderDto.setCurrentTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                .withLocation(new Location(8L, "Москва")).build());

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        assertThat(orderService.getSuitableDriversForOrder(foundOrderId)).isEmpty();
    }

    @Test
    void completeOrderShouldProperlyDeleteOrderFromDriversAndTruck() {
        final Long foundOrderId = 1L;
        final short orderDistance = 706;
        final Driver driver = Driver.builder()
                .withId(1L)
                .withLocation(new Location(8L, "Москва"))
                .withMonthWorkedHours((short)12).build();
        final Order.Waypoint firstWaypoint = Order.Waypoint.builder()
                .withId(1L)
                .withType(WaypointType.LOADING)
                .withIsDone(false)
                .withLocation(new Location(8L, "Москва"))
                .withCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(100).build())
                .build();
        final Order.Waypoint secondWaypoint = Order.Waypoint.builder()
                .withId(2L)
                .withType(WaypointType.SHIPPING)
                .withLocation(new Location(9L, "Санкт-Петербург"))
                .withIsDone(false)
                .withCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(100).build())
                .build();
        final WaypointDto firstWaypointDto = new WaypointDto();

        firstWaypointDto.setId(1L);
        firstWaypointDto.setType(WaypointType.LOADING.ordinal());
        firstWaypointDto.setIsDone(false);
        firstWaypointDto.setLocation(new Location(8L, "Москва"));
        firstWaypointDto.setCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(100).build());

        final WaypointDto secondWaypointDto = new WaypointDto();

        secondWaypointDto.setId(2L);
        secondWaypointDto.setType(WaypointType.SHIPPING.ordinal());
        secondWaypointDto.setIsDone(false);
        secondWaypointDto.setLocation(new Location(9L, "Санкт-Петербург"));
        secondWaypointDto.setCargo(Cargo.builder().withId(1L).withName("Панели").withWeight(100).build());

        final Order foundOrder = Order.builder()
                .withId(foundOrderId)
                .withStatus(false)
                .withStartDate(LocalDate.of(2021, 12, 12))
                .withWaypoints(Arrays.asList(firstWaypoint, secondWaypoint))
                .withTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                        .withLocation(new Location(8L, "Москва")).build())
                .withDrivers(Collections.singletonList(driver))
                .build();

        final OrderDto foundOrderDto = new OrderDto();

        foundOrderDto.setId(foundOrderId);
        foundOrderDto.setIsCompleted(false);
        foundOrderDto.setStartDate(LocalDate.of(2021, 12, 12));
        foundOrderDto.setWaypoints(Arrays.asList(firstWaypoint, secondWaypoint));
        foundOrderDto.setCurrentTruck(Truck.builder().withId(1L).withRegNumber("RG32353")
                .withLocation(new Location(8L, "Москва")).build());
        foundOrderDto.setCurrentDrivers(Collections.singletonList(driver));

        when(orderDao.findById(foundOrderId)).thenReturn(Optional.ofNullable(foundOrder));
        when(orderMapper.mapEntityToDto(foundOrder)).thenReturn(foundOrderDto);

        final WaypointsPair waypointsPair = new WaypointsPair();
        waypointsPair.setPair(Arrays.asList(firstWaypointDto, secondWaypointDto));

        when(waypointService.getAllWaypointsPairByOrderId(foundOrderId)).thenReturn(Collections.singletonList(waypointsPair));
        when(locationMapService.getDistanceBetweenLocationsByIds(firstWaypointDto.getLocation().getId(),
                secondWaypointDto.getLocation().getId())).thenReturn(orderDistance);

        doNothing().when(cargoService).deleteCargo(secondWaypoint.getCargo().getId());

        doNothing().when(orderDao).deleteById(foundOrderId);

        assertThatNoException().isThrownBy(() -> orderService.completeOrder(foundOrderId));

        verify(orderDao).deleteById(foundOrderId);
    }

}
*/