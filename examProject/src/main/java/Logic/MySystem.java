package Logic;

import DAO.*;
import DAO.Impl.*;
import DAO.SessionFactory.SessionFactoryUtil;
import Entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MySystem {
    private SessionFactory sessionFactory;
    private IClientDao clientDao;
    private ICoachDao coachDao;
    private IRefereeDao refereeDao;
    private ISportsmanDao sportsmanDao;
    private IHallDao hallDao;
    private IKindSportDao kindSportDao;
    private ISportGroupDao sportGroupDao;
    private Scanner in;
    private  IUserDao userDao;
    public MySystem() {
        sessionFactory = SessionFactoryUtil.getSessionFactory();
        clientDao = new ClientDao();
        coachDao = new CoachDao();
        refereeDao = new RefereeDao();
        sportsmanDao = new SportsmanDao();
        hallDao = new HallDao();
        kindSportDao = new KindSportDao();
        sportGroupDao = new SportGroupDao();
        in = new Scanner(java.lang.System.in);
        userDao = new UserDao();
    }
    public void registerUser() throws JsonProcessingException {
        in.nextLine();
        java.lang.System.out.println("Придумайте логин:");
        String login = in.nextLine();
        java.lang.System.out.println("Придумайте пароль:");
        int passwordHashCode = in.nextLine().hashCode();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(new User(login, passwordHashCode));
        transaction.commit();
        session.close();
        menu();
    }
    public void startSystem() throws JsonProcessingException {
        java.lang.System.out.println("Здравствуйте! Что вы хотите?");
        java.lang.System.out.println("1 - Войти в систему ");
        java.lang.System.out.println("2 - Зарегистрироваться в системе");
        java.lang.System.out.println("3 - Выйти из системы");
        try {
            switch (in.nextInt()) {
                case 1: {
                    logIn();
                }
                break;
                case 2: {
                    registerUser();
                }
                break;
                case 3: {
                    java.lang.System.out.println("До свидания!");
                    java.lang.System.exit(0);
                } break;
                default: {
                    java.lang.System.out.println("Некорректная операция! Попробуте еще раз!");
                    startSystem();
                }
                break;
            }
        }
        catch (InputMismatchException e ) {
            System.out.println("Вводить можно только цифры !");
            startSystem();
        }
    }

    public void logIn() throws JsonProcessingException {
        in.nextLine();
        System.out.println("Ваш логин:");
        String login = in.nextLine();
        System.out.println("Ваш пароль:");
        int passwordHashCode = in.nextLine().hashCode();
        if (userDao.getNeedUsers(login, passwordHashCode).size() > 0) {
            menu();
        } else {
            System.out.println("Неправильные параметры авторизации! Давайте попробуем еще раз!");
            logIn();
        }
    }
    public void printList(List<? extends Object> list ) throws JsonProcessingException {
        for (Object item: list) {
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(item);
            System.out.println(json);
        }
    }

    public void menuClient() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить клиента");
        System.out.println("2 - Просмотреть список клиентов");
        System.out.println("3 - Выполнить поиск клиентов");
        System.out.println("4 - Удалить клиента");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    System.out.println("Имя клиента:");
                    String firstName = in.nextLine();
                    System.out.println("Фамилия клиента:");
                    String lastName = in.nextLine();
                    System.out.println("Номер телефона клиента:");
                    String telephoneNumber = in.nextLine();
                    clientDao.addClient(new Client(firstName, lastName, telephoneNumber));
                    System.out.println("Клиент успешно добавлен!");
                    menuClient();
                }
                break;
                case 2: {
                    List<Client> clientList = clientDao.getAllClients();
                    if (clientList.size() > 0) {
                        printList(clientList);
                    } else {
                        System.out.println("Список клиентов пуст!");
                    }
                    menuClient();
                }
                break;
                case 3: {
                    System.out.println("Критерий поиска:");
                    System.out.println("1 - ID");
                    System.out.println("2 - Имя");
                    System.out.println("3 - Фамилия");
                    switch (in.nextInt()) {
                        case 1: {
                            System.out.println("ID клиента:");
                            int id = in.nextInt();
                            List<Client> clientList = clientDao.searchClientById(id);
                            if (clientList.size() > 0) {
                                printList(clientList);
                            } else {
                                System.out.println("Поиск клиентов по ID не дал результатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            System.out.println("Имя клиента:");
                            String firstName = in.nextLine();
                            List<Client> clientList = clientDao.searchClientByFirstName(firstName);
                            if (clientList.size() > 0) {
                                printList(clientList);
                            } else {
                                System.out.println("Поиск клиентов по имени не дал результатов!");
                            }
                        }
                        break;
                        case 3: {
                            in.nextLine();
                            System.out.println("Фамилия клиента:");
                            String lastName = in.nextLine();
                            List<Client> clientList = clientDao.searchClientByLastName(lastName);
                            if (clientList.size() > 0) {
                                printList(clientList);
                            } else {
                                System.out.println("Поиск клиентов по фамилии не дал результатов!");
                            }
                        }
                        break;
                        default: {
                            java.lang.System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuClient();
                }
                break;
                case 4: {
                    System.out.println("ID удаляемого клиента:");
                    int id = in.nextInt();
                    System.out.println("Вы действительно хотите удалить этого клиента?");
                    System.out.println("1 - Да");
                    java.lang.System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (clientDao.searchClientById(id).size() > 0) {
                                clientDao.deleteClientById(id);
                                System.out.println("Клиент успешно удален!");
                            } else {
                                System.out.println("Клиента с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: {
                        }
                        break;
                        default: {
                            System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                    }
                    menuClient();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuClient();
                }
                break;
            }
        }
        catch (InputMismatchException e) {
            java.lang.System.out.println("Вводить можно только цифры!");
            menuClient();
        }
    }

    public void menuCoach() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить тренера");
        System.out.println("2 - Просмотреть список тренеров");
        System.out.println("3 - Выполнить поиск тренеров");
        System.out.println("4 - Удалить тренера");
        System.out.println("5 - Вернуться в меню");
        System.out.println("6 - Добавить компетенцию");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    java.lang.System.out.println("Имя тренера");
                    String firstName = in.nextLine();
                    java.lang.System.out.println("Фамилия тренера:");
                    String lastName = in.nextLine();
                    java.lang.System.out.println("Номер телефона тренера:");
                    String telephoneNumber = in.nextLine();
                    coachDao.addCoach(new Coach(firstName, lastName, telephoneNumber));
                    System.out.println("Тренер успешно добавлен!");
                    menuCoach();
                }
                break;
                case 2: {
                    List<Coach> coachList = coachDao.getAllCoaches();
                    if (coachList.size() > 0) {
                        printList(coachList);
                    } else {
                        System.out.println("Список тренеров пуст!");
                    }
                    menuCoach();
                }
                break;
                case 3: {
                    System.out.println("Критерий поиска:");
                    System.out.println("1 - ID");
                    System.out.println("2 - Имя");
                    System.out.println("3 - Фамилия");
                    switch (in.nextInt()) {
                        case 1: {
                            java.lang.System.out.println("ID тренера:");
                            int id = in.nextInt();
                            List<Coach> coachList = coachDao.searchCoachById(id);
                            if (coachList.size() > 0) {
                                printList(coachList);
                            } else {
                                System.out.println("Поиск тренеров по ID не дал результатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            System.out.println("Имя тренера:");
                            String firstName = in.nextLine();
                            List<Coach> coachList = coachDao.searchCoachByFirstName(firstName);
                            if (coachList.size() > 0) {
                                printList(coachList);
                            } else {
                                System.out.println("Поиск тренеров по имени не дал результатов!");
                            }
                        }
                        break;
                        case 3: {
                            in.nextLine();
                            System.out.println("Фамилия тренера:");
                            String lastName = in.nextLine();
                            List<Coach> coachList = coachDao.searchCoachByLastName(lastName);
                            if (coachList.size() > 0) {
                                printList(coachList);
                            } else {
                                System.out.println("Поиск тренеров по фамилии не дал результатов!");
                            }
                        }
                        break;
                        default: {
                            System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuCoach();
                }
                break;
                case 4: {
                    System.out.println("ID тренера:");
                    int id = in.nextInt();
                    System.out.println("Вы действительно хотите удалить этого  тренера ?");
                    System.out.println("1 - Да");
                    System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (coachDao.searchCoachById(id).size() > 0) {
                                coachDao.deleteCoachById(id);
                                System.out.println("Тренер успешно удален!");
                            } else {
                                System.out.println("Тренера с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: { }break;
                    }
                    menuCoach();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                case 6: {
                    System.out.println("ID тренера:");
                    int idCoach = in.nextInt();
                    System.out.println("ID вида спорта:");
                    int idKind = in.nextInt();
                    if (coachDao.searchCoachById(idCoach).size()>0 && kindSportDao.searchKindById(idKind).size()>0) {
                        coachDao.addKindByCoach(idCoach, idKind);
                        System.out.println("Компетенция успешно добавлена!");
                    } else {
                        System.out.println("Тренера/вида спорта с таким ID не существует!");
                    }
                    menuCoach();
                }
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuCoach();
                }
                break;
            }
        }
        catch (InputMismatchException e) {
            java.lang.System.out.println("Вводить можно только цифры!");
            menuCoach();
        }
    }

    public void menuReferee() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить судью");
        System.out.println("2 - Просмотреть список судей");
        System.out.println("3 - Выполнить поиск судей");
        System.out.println("4 - Удалить судью ");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    java.lang.System.out.println("Имя судьи:");
                    String firstName = in.nextLine();
                    java.lang.System.out.println("Фамилия судьи:");
                    String lastName = in.nextLine();
                    java.lang.System.out.println("Номер телефона судьи:");
                    String telephoneNumber = in.nextLine();
                    java.lang.System.out.println("Категория судьи:");
                    String category = in.nextLine();
                    refereeDao.addReferee(new Referee(firstName, lastName, telephoneNumber, category));
                    java.lang.System.out.println("Судья успешно добавлен!");
                    menuReferee();
                }
                break;
                case 2: {
                    List<Referee> refereeList = refereeDao.getAllReferees();
                    if (refereeList.size() > 0) {
                        printList(refereeList);
                    } else {
                        java.lang.System.out.println("Список судей пуст!");
                    }
                    menuReferee();
                }
                break;
                case 3: {
                    java.lang.System.out.println("Критерий поиска:");
                    java.lang.System.out.println("1 - ID");
                    java.lang.System.out.println("2 - Имя");
                    java.lang.System.out.println("3 - Фамилия");
                    switch (in.nextInt()) {
                        case 1: {
                            java.lang.System.out.println("ID судьи:");
                            int id = in.nextInt();
                            List<Referee> refereeList = refereeDao.searchRefereeById(id);
                            if (refereeList.size() > 0) {
                                printList(refereeList);
                            } else {
                                java.lang.System.out.println("Поиск судей по ID не дал резултатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            java.lang.System.out.println("Имя судьи:");
                            String firstName = in.nextLine();
                            List<Referee> refereeList = refereeDao.searchRefereeByFirstName(firstName);
                            if (refereeList.size() > 0) {
                                printList(refereeList);
                            } else {
                                java.lang.System.out.println("Поиск судей по имени не дал резултатов!");
                            }
                        }
                        break;
                        case 3: {
                            in.nextLine();
                            java.lang.System.out.println("Фамилия судьи:");
                            String lastName = in.nextLine();
                            List<Referee> refereeList = refereeDao.searchRefereeByLastName(lastName);
                            if (refereeList.size() > 0) {
                                printList(refereeList);
                            } else {
                                java.lang.System.out.println("Поиск судей по фамилии не дал резултатов!");
                            }
                        }
                        break;
                        default: {
                            java.lang.System.out.println("Некорректна операция! Давайте еще раз!");
                        }
                    }
                    menuReferee();
                }
                break;
                case 4: {
                    java.lang.System.out.println("ID удаляемого судьи:");
                    int id = in.nextInt();
                    java.lang.System.out.println("Вы действительно хотите удалить этого судью?");
                    java.lang.System.out.println("1 - Да");
                    java.lang.System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (refereeDao.searchRefereeById(id).size() > 0) {
                                refereeDao.deleteRefereeById(id);
                                java.lang.System.out.println("Судья успешно удален!");
                            } else {
                                java.lang.System.out.println("Судьи с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: {
                        }
                        break;
                        default: {
                            java.lang.System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                    }
                    menuReferee();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                default: {
                    java.lang.System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuReferee();
                }
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Вводить можно только цифры!");
            menuReferee();
        }
    }

    public void menuSportsman() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить спортсмена");
        System.out.println("2 - Просмотреть список спортсменов");
        System.out.println("3 - Выполнить поиск спортсменов");
        System.out.println("4 - Удалить спортсмена");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    java.lang.System.out.println("Имя спортсмена:");
                    String firstName = in.nextLine();
                    java.lang.System.out.println("Фамилия спортсмена:");
                    String lastName = in.nextLine();
                    java.lang.System.out.println("Номер телефона спортсмена:");
                    String telephoneNumber = in.nextLine();
                    java.lang.System.out.println("Разряд/звание спортсмена:");
                    String discharge = in.nextLine();
                    sportsmanDao.addSportsman(new Sportsman(firstName, lastName, telephoneNumber, discharge));
                    java.lang.System.out.println("Спортсмен успешно добавлен!");
                    menuSportsman();
                }
                break;
                case 2: {
                    List<Sportsman> sportsmanList = sportsmanDao.getAllSportsmans();
                    if (sportsmanList.size() > 0) {
                        printList(sportsmanList);
                    } else {
                        java.lang.System.out.println("Список спортсменов пуст!");
                    }
                    menuSportsman();
                }
                break;
                case 3: {
                    java.lang.System.out.println("Критерий поиска:");
                    java.lang.System.out.println("1 - ID");
                    java.lang.System.out.println("2 - Имя");
                    java.lang.System.out.println("3 - Фамилия");
                    switch (in.nextInt()) {
                        case 1: {
                            java.lang.System.out.println("ID спортсмена:");
                            int id = in.nextInt();
                            List<Sportsman> sportsmanList = sportsmanDao.searchSportsmanById(id);
                            if (sportsmanList.size() > 0) {
                                printList(sportsmanList);
                            } else {
                                java.lang.System.out.println("Поиск спортсменов по ID не дал результатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            java.lang.System.out.println("Имя спортсмена:");
                            String firstName = in.nextLine();
                            List<Sportsman> sportsmanList = sportsmanDao.searchSportsmanByFirstName(firstName);
                            if (sportsmanList.size() > 0) {
                                printList(sportsmanList);
                            } else {
                                java.lang.System.out.println("Поиск спортсменов по имени не дал результатов!");
                            }
                        }
                        break;
                        case 3: {
                            in.nextLine();
                            java.lang.System.out.println("Фамилия спортсмена:");
                            String lastName = in.nextLine();
                            List<Sportsman> sportsmanList = sportsmanDao.searchSportsmanByLastName(lastName);
                            if (sportsmanList.size() > 0) {
                                printList(sportsmanList);
                            } else {
                                java.lang.System.out.println("Поиск спортсменов по фамилии не дал результатов!");
                            }
                        }
                        break;
                        default: {
                            java.lang.System.out.println("Некорректная операция! Попробуйте еще раз!");
                        }
                        break;
                    }
                    menuSportsman();
                }
                break;
                case 4: {
                    java.lang.System.out.println("ID удаляемого спортсмена:");
                    int id = in.nextInt();
                    java.lang.System.out.println("Вы действительно хотите удалить этого спортсмена?");
                    java.lang.System.out.println("1 - Да");
                    java.lang.System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (sportsmanDao.searchSportsmanById(id).size() > 0) {
                                sportsmanDao.deleteSportsmanById(id);
                                java.lang.System.out.println("Спортсмен успешно удален!");
                            } else {
                                java.lang.System.out.println("Спортсмена с таким ID не сущетвует!");
                            }
                        }
                        break;
                        case 2: { } break;
                        default: {
                            java.lang.System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuSportsman();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuSportsman();
                }
            }
        }
        catch(InputMismatchException e) {
            System.out.println("Вводить можно только цифры! ");
            menuSportsman();
        }
    }

    public void menuHall() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить зал");
        System.out.println("2 - Просмотреть список залов");
        System.out.println("3 - Выполнить поиск залов");
        System.out.println("4 - Удалить зал");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    System.out.println("Название зала:");
                    String nameHall = in.nextLine();
                    hallDao.addHall(new Hall(nameHall));
                    System.out.println("Зал успешно добавлен!");
                    menuHall();
                }
                break;
                case 2: {
                    List<Hall> hallList = hallDao.getAllHalls();
                    if (hallList.size() > 0) {
                        printList(hallList);
                    } else {
                        System.out.println("Список залов пуст!");
                    }
                    menuHall();
                }
                break;
                case 3: {
                    System.out.println("Критерий поиска:");
                    System.out.println("1 - ID");
                    System.out.println("2 - Название");
                    switch (in.nextInt()) {
                        case 1: {
                            java.lang.System.out.println("ID зала:");
                            int id = in.nextInt();
                            List<Hall> hallList = hallDao.searchHallById(id);
                            if (hallList.size() > 0) {
                                printList(hallList);
                            } else {
                                System.out.println("Поиск залов по ID не дал результатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            System.out.println("Название зала:");
                            String name = in.nextLine();
                            List<Hall> hallList = hallDao.searchHallByName(name);
                            if (hallList.size() > 0) {
                                printList(hallList);
                            } else {
                                System.out.println("Поиск залов по названию не дал результатов!");
                            }
                        }
                        break;
                        default: {
                            System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuHall();
                }
                break;
                case 4: {
                    System.out.println("ID удаляемого зала:");
                    int id = in.nextInt();
                    System.out.println("Вы действительно хотите удалить этот зал?");
                    System.out.println("1 - Да");
                    System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (hallDao.searchHallById(id).size() > 0) {
                                hallDao.deleteHallById(id);
                                System.out.println("Зал успешно удален!");
                            } else {
                                System.out.println("Зала с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: { }break;
                        default: {
                            System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                    }
                    menuHall();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                default: {
                    System.out.println("Некорректная операция ! Попробуйте еще раз!");
                    menuHall();
                }
                break;
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Вводить можно только цифры! Попробуйте еще раз!");
            menuHall();
        }
    }
    public void menuKindSport() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить вид спорта");
        System.out.println("2 - Просмотреть список видов спорта");
        System.out.println("3 - Выполнить поиск видов спорта");
        System.out.println("4 - Удалить вид спорта");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    System.out.println("Название вида спорта:");
                    String nameKind = in.nextLine();
                    kindSportDao.addKindSport(new KindSport(nameKind));
                    System.out.println("Вид спорта успешно добавлен!");
                    menuKindSport();
                }
                break;
                case 2: {
                    List<KindSport> kindSportList = kindSportDao.getAllKinds();
                    if (kindSportList.size() > 0) {
                        printList(kindSportList);
                    } else {
                        System.out.println("Список видов спорта пуст!");
                    }
                    menuKindSport();
                }
                break;
                case 3: {
                    System.out.println("Критерий поиска:");
                    System.out.println("1 - ID");
                    System.out.println("2 - Название");
                    switch (in.nextInt()) {
                        case 1: {
                            System.out.println("ID вида спорта:");
                            int id = in.nextInt();
                            List<KindSport> kindSportList = kindSportDao.searchKindById(id);
                            if (kindSportList.size() > 0) {
                                printList(kindSportList);
                            } else {
                                System.out.println("Поиск видов спорта по ID не дал результатов! Скорее всего, вида  спорта  с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            System.out.println("Название вида спорта:");
                            String name = in.nextLine();
                            List<KindSport> kindSportList = kindSportDao.searchKindByName(name);
                            if (kindSportList.size() > 0) {
                                printList(kindSportList);
                            } else {
                                System.out.println("Поиск видов спорта по названию не дал результатов! Возможно, вида спорта с таким названием не существует!");
                            }
                        }
                        break;
                        default: {
                            System.out.println("Некорретктная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuKindSport();
                }
                break;
                case 4: {
                    System.out.println("ID удаляемого вида спорта:");
                    int id = in.nextInt();
                    System.out.println("Вы действительно хотите удалить этот вид спорта?");
                    System.out.println("1 - Да");
                    System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (kindSportDao.searchKindById(id).size() > 0) {
                                kindSportDao.deleteKindSportById(id);
                                System.out.println("Вид спорта успешно удален!");
                            } else {
                                System.out.println("Вида спорта с таким ID не существует!");
                            }
                        }
                        break;
                        case 2: {
                        }
                        break;
                        default: {
                            System.out.println("Некорректная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuKindSport();
                }
                break;
                case 5: {
                    menu();
                }
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuKindSport();
                }
                break;
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Вводить можно только цифры!");
            menuKindSport();
        }
    }
    public void menuSportGroup() throws JsonProcessingException {
        System.out.println("Выберите одно из следующих действий:");
        System.out.println("1 - Добавить группу");
        System.out.println("2 - Просмотреть список групп");
        System.out.println("3 - Выполнить поиск групп");
        System.out.println("4 - Удалить группу");
        System.out.println("5 - Вернуться в меню");
        try {
            switch (in.nextInt()) {
                case 1: {
                    in.nextLine();
                    System.out.println("Название группы:");
                    String name = in.nextLine();
                    System.out.println("День недели:");
                    int workingDay = in.nextInt();
                    sportGroupDao.addSportGroup(new SportGroup(name, workingDay));
                    System.out.println("Группа успешно добавена!");
                    menuSportGroup();
                }
                break;
                case 2: {
                    List<SportGroup> sportGroupList = sportGroupDao.getAllSportGroups();
                    if (sportGroupList.size() > 0) {
                        printList(sportGroupList);
                    } else {
                        System.out.println("Список групп пуст!");
                    }
                    menuSportGroup();
                }
                break;
                case 3: {
                    System.out.println("Критерий поиска:");
                    System.out.println("1 - ID");
                    System.out.println("2 - Название");
                    switch (in.nextInt()) {
                        case 1: {
                            System.out.println("ID группы:");
                            int id = in.nextInt();
                            List<SportGroup> sportGroupList = sportGroupDao.searchGroupById(id);
                            if (sportGroupList.size() > 0) {
                                printList(sportGroupList);
                            } else {
                                System.out.println("Поиск групп по ID не дал результатов!");
                            }
                        }
                        break;
                        case 2: {
                            in.nextLine();
                            System.out.println("Название группы:");
                            String name = in.nextLine();
                            List<SportGroup> sportGroupList = sportGroupDao.searchGroupByName(name);
                            if (sportGroupList.size() > 0) {
                                printList(sportGroupList);
                            } else {
                                System.out.println("Поиск групп по названию не дал результатов!");
                            }
                        }
                        break;
                        default: {
                            System.out.println("Некорретная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuSportGroup();
                }
                break;
                case 4: {
                    System.out.println("ID удаляемой группы:");
                    int id = in.nextInt();
                    System.out.println("Вы действительно хотите удалить эту группу?");
                    System.out.println("1 - Да");
                    System.out.println("2 - Нет");
                    switch (in.nextInt()) {
                        case 1: {
                            if (sportGroupDao.searchGroupById(id).size() > 0) {
                                sportGroupDao.deleteSportGroupById(id);
                                System.out.println("Группа успешно удалена!");
                            } else {
                                System.out.println("Группы c таким ID не существует!");
                            }
                        }
                        break;
                        case 2: {
                        }
                        break;
                        default: {
                            System.out.println("Некорретная операция! Давайте еще раз!");
                        }
                        break;
                    }
                    menuSportGroup();
                }
                break;
                case 5: {
                    menu();
                }
                break;
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menuSportGroup();
                }
                break;
            }
        }
        catch(InputMismatchException e) {
            System.out.println("Вводить можно только цифры!");
            menuSportGroup();
        }
    }
    public void menu() throws JsonProcessingException {
        System.out.println("Выберите одну из сущностей:");
        System.out.println("1 - Клиент");
        System.out.println("2 - Тренер");
        System.out.println("3 - Судья");
        System.out.println("4 - Спортсмен");
        System.out.println("5 - Зал");
        System.out.println("6 - Вид спорта");
        System.out.println("7 - Группа");
        System.out.println("8 - Выйти из аккаунта");
        try {
            switch (in.nextInt()) {
                case 1: {
                    menuClient();
                }
                break;
                case 2: {
                    menuCoach();
                }
                break;
                case 3: {
                    menuReferee();
                }
                break;
                case 4: {
                    menuSportsman();
                }
                break;
                case 5: {
                    menuHall();
                }
                break;
                case 6: {
                    menuKindSport();
                }
                break;
                case 7: {
                    menuSportGroup();
                }
                break;
                case 8 : {
                    startSystem();
                } break;
                default: {
                    System.out.println("Некорректная операция! Попробуйте еще раз!");
                    menu();
                }
                break;
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Вводить можно только цифры!");
            menu();
        }
    }

}
