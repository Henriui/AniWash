package aniwash.entity;

import aniwash.dao.AnimalDao;
import aniwash.dao.IAnimalDao;
import aniwash.datastorage.DatabaseConnector;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnimalDAO: tietokantatoimintojen (CRUD) testaus")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimalDaoTest {

    private static IAnimalDao animalDao;

    private final String aNimi = "Haukku";
    private final String aTyyppi = "Koira";
    private final String aRotu = "Sekarotu";
    private final String aDescription = "Värikäs ja iloinen koira, joka rakastaa ihmisiä ja leikkejä - sietäisi laittaa piikille.";

    @BeforeAll
    public static void initAll() {
        DatabaseConnector.openDbConnection("com.aniwash.test");
        animalDao = new AnimalDao();
    }

    private Animal animal = new Animal(aNimi, aTyyppi, aRotu, aDescription);

    @BeforeEach
    public void setUp() {
        animal = new Animal(aNimi, aTyyppi, aRotu, aDescription);
    }

    @AfterEach
    public void tearDown() {
        for (Animal animal : animalDao.findAllAnimals()) {
            animalDao.deleteByIdAnimal(animal.getId());
        }
    }

/*
    @AfterAll
    public static void tearDownAll() {
        DatabaseConnector.closeDbConnection();
    }
*/

    @Test
    @DisplayName("Tässä kaikki testit yhdessä metodissa.")
    public void testDAOAnimal() {
        // Jos joku testi ei mene läpi, tämän metodin suoritus päättyy heti.
        // Lisää animal
        assertTrue(animalDao.addAnimal(animal), "addCustomer(): Uuden customerin lisääminen ei onnistu.");
        assertFalse(animalDao.addAnimal(animal), "addCustomer(): Saman customerin pystyy lisäämään kahteen kertaan.");
        long id = animal.getId();
        assertTrue(animalDao.findAllAnimals().size() > 0, "findAllCustomer(): Ei löydy yhtään asiakasta.");
        // Nyt haun tulee onnistua ja kenttien tulee tietenkin olla asetettu oikein
        assertNotNull((animal = animalDao.findByIdAnimal(id)), "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, animal.getId(), "getId(): animal id tunnus väärin.");
        assertEquals(aNimi, animal.getName(), "getName(): animal nimi väärin.");
        assertEquals(aTyyppi, animal.getType(), "getType(): animal tyyppi väärin.");
        assertEquals(aRotu, animal.getBreed(), "getBreed(): animalin rotu väärin.");
        assertEquals(aDescription, animal.getDescription(), "getDescription(): animalin kuvaus on virheellinen.");
        // animalUpdate() muutoksen tulee onnistua
        animal.setName("Maukku");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Maukku", animal.getName(), "getName(): Eläimen nimi päivitys ei onnistunut.");
        animal.setType("Kissa");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Kissa", animal.getType(), "getType(): Eläimen tyyppi päivitys ei onnistunut.");
        animal.setBreed("Villikissa");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Villikissa", animal.getBreed(), "getBreed(): Eläimen rodun päivittäminen ei onnistunut.");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        animal.setDescription("mjäyy");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("mjäyy", animal.getDescription(), "getDescription(): Eläimen kuvaus kentän päivitys ei onnistunut.");
        // Testissä lisätyn customerin poiston tulee onnistua
        assertTrue(animalDao.deleteByIdAnimal(animal.getId()), "deleteCustomerById(): Asiakkaan poisto ei onnistunut.");
        assertNull(animalDao.findByIdAnimal(animal.getId()), "deleteCustomerById(): Asiakkaan poisto ei onnistunut - asiakan voitiin hakea tietokannasta.");
        // Olemattoman valuutan poiston tulee "epäonnistua"
        assertFalse(animalDao.deleteByIdAnimal(0L), "deleteCustomerById(): Väittää poistaneensa olemattoman asiakkaan.");
    }

    @Test
    @Order(1)
    @DisplayName("Lisätään eläin tietokantaan")
    public void testAddAnimal() {
        assertTrue(animalDao.addAnimal(animal));
    }

    @Test
    @DisplayName("Samaa asiakasta ei saa lisätä toistamiseen")
    @Order(2)
    public void testAddAnimal2() {
        assertTrue(animalDao.addAnimal(animal), "addAnimal(): Uuden eläimen lisääminen ei onnistu.");
        assertFalse(animalDao.addAnimal(animal), "addAnimal(): Saman eläimen voi lisätä kahteen kertaan.");
    }

    @Test
    @Order(3)
    @DisplayName("Etsitään lisätty eläin tietokannasta")
    public void testFindByIdAnimal() {
        animalDao.addAnimal(animal);
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        animal = animalDao.findByNameAnimal(aNimi);
        assertNotNull(animal, "addAnimal(): Juuri lisätyn elukan hakeminen ei onnistunut");
        assertEquals(aNimi, animal.getName(), "getName(): Elukan nimi on väärin.");
        assertEquals(aRotu, animal.getBreed(), "getBreed(): Rotu on väärin.");
    }

    @Test
    @Order(4)
    @DisplayName("Etsitään eläin nimen perusteella")
    public void testFindByNameAnimal() {
        animalDao.addAnimal(animal);
        Animal animal2 = animalDao.findByNameAnimal(animal.getName());
        assertEquals(animal, animal2);
    }

    @Test
    @Order(5)
    @DisplayName("Etsitään kaikki eläimet - tietokannassa on yksi eläin")
    public void testFindAllAnimal() {
        animalDao.addAnimal(animal);
        assertTrue(animalDao.findAllAnimals().size() > 0);
    }

    @Test
    @Order(6)
    @DisplayName("Etsitään kaikki eläimet - tietokannassa ei ole eläimiä")
    public void testFindAllAnimal2() {
        animalDao.addAnimal(animal);
        List<Animal> animals = animalDao.findAllAnimals();
        for (Animal a : animals) {
            animalDao.deleteByIdAnimal(a.getId());
        }
        assertEquals(0, animalDao.findAllAnimals().size());
    }

    @Test
    @Order(7)
    @DisplayName("Päivitetään eläimen nimi")
    public void testUpdateAnimalName() {
        animalDao.addAnimal(animal);
        animal.setName("Maukku");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Maukku", animal.getName(), "getName(): Eläimen nimi päivitys ei onnistunut.");
    }

    @Test
    @Order(8)
    @DisplayName("Päivitetään eläimen tyyppi")
    public void testUpdateAnimalType() {
        animalDao.addAnimal(animal);
        animal.setType("Kissa");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Kissa", animal.getType(), "getType(): Eläimen tyypin päivitys ei onnistunut.");
    }

    @Test
    @Order(9)
    @DisplayName("Päivitetään eläimen rotu")
    public void testUpdateAnimalBreed() {
        animalDao.addAnimal(animal);
        animal.setBreed("Villikissa");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("Villikissa", animal.getBreed(), "getBreed(): Eläimen rodun päivittäminen ei onnistunut.");
    }

    @Test
    @Order(11)
    @DisplayName("Päivitetään eläimen kuvaus")
    public void testUpdateAnimalDescription() {
        animalDao.addAnimal(animal);
        animal.setDescription("mjäyy");
        assertTrue(animalDao.updateAnimal(animal));
        animal = animalDao.findByIdAnimal(animal.getId());
        assertEquals("mjäyy", animal.getDescription(), "getDescription(): Eläimen kuvaus kentän päivitys ei onnistunut.");
    }

    @Test
    @Order(12)
    @DisplayName("Poistetaan eläin tietokannasta")
    public void testDeleteByIdAnimal() {
        animalDao.addAnimal(animal);
        assertTrue(animalDao.deleteByIdAnimal(animal.getId()));
    }

    @Test
    @Order(13)
    @DisplayName("Etsitään poistettu eläin")
    public void testFindByIdAnimal2() {
        animalDao.addAnimal(animal);
        animalDao.deleteByIdAnimal(animal.getId());
        assertNull(animalDao.findByIdAnimal(animal.getId()), "deleteByIdAnimal(): Animalin poisto ei onnistunut - eläin voitiin hakea tietokannasta.");
    }

    @Test
    @Order(14)
    @DisplayName("Etsitään poistettu eläin nimen perusteella")
    public void testFindByNameAnimal2() {
        animalDao.addAnimal(animal);
        animalDao.deleteByIdAnimal(animal.getId());
        assertNull(animalDao.findByNameAnimal(animal.getName()));
    }

    @Test
    @DisplayName("Olemattoman eläimen poistoyrityksen tulee palauttaa false")
    @Order(15)
    public void testDeleteByIdAnimal2() {
        assertFalse(animalDao.deleteByIdAnimal(0L));
    }

    @Test
    @DisplayName("Olemattoman eläimen päivitysyrityksen tulee palauttaa false")
    @Order(16)
    public void testUpdateAnimal2() {
        animal.setId(9999L);
        assertFalse(animalDao.updateAnimal(animal));
    }

    @Test
    @DisplayName("Eläimen pehmeä poistaminen databasesta")
    @Order(17)
    public void testSoftDeleteAnimal() {
        animalDao.addAnimal(animal);
        animal.setDeleted();
        assertTrue(animalDao.updateAnimal(animal), "Päivitetyn eläimen tallennus ei onnistunut");
        assertEquals(1, animalDao.findByIdAnimal(animal.getId()).isDeleted(), "Eläimen pehmeä poistaminen ei onnistunut");
    }

}
