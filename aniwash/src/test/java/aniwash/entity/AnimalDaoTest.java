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
        for (Animal animal : animalDao.findAll()) {
            animalDao.deleteById(animal.getId());
        }
    }

    @Test
    @DisplayName("Tässä kaikki testit yhdessä metodissa.")
    public void testDAOAnimal() {
        // Jos joku testi ei mene läpi, tämän metodin suoritus päättyy heti.
        // Lisää animal
        assertTrue(animalDao.add(animal), "addCustomer(): Uuden customerin lisääminen ei onnistu.");
        assertFalse(animalDao.add(animal), "addCustomer(): Saman customerin pystyy lisäämään kahteen kertaan.");
        long id = animal.getId();
        assertTrue(animalDao.findAll().size() > 0, "findAllCustomer(): Ei löydy yhtään asiakasta.");
        // Nyt haun tulee onnistua ja kenttien tulee tietenkin olla asetettu oikein
        assertNotNull((animal = animalDao.findById(id)), "findCustomerById(): Juuri lisätyn asiakkaan hakeminen ei onnistunut");
        assertEquals(id, animal.getId(), "getId(): animal id tunnus väärin.");
        assertEquals(aNimi, animal.getName(), "getName(): animal nimi väärin.");
        assertEquals(aTyyppi, animal.getType(), "getType(): animal tyyppi väärin.");
        assertEquals(aRotu, animal.getBreed(), "getBreed(): animalin rotu väärin.");
        assertEquals(aDescription, animal.getDescription(), "getDescription(): animalin kuvaus on virheellinen.");
        // animalUpdate() muutoksen tulee onnistua
        animal.setName("Maukku");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Maukku", animal.getName(), "getName(): Eläimen nimi päivitys ei onnistunut.");
        animal.setType("Kissa");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Kissa", animal.getType(), "getType(): Eläimen tyyppi päivitys ei onnistunut.");
        animal.setBreed("Villikissa");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Villikissa", animal.getBreed(), "getBreed(): Eläimen rodun päivittäminen ei onnistunut.");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        animal.setDescription("mjäyy");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("mjäyy", animal.getDescription(), "getDescription(): Eläimen kuvaus kentän päivitys ei onnistunut.");
        // Testissä lisätyn customerin poiston tulee onnistua
        assertTrue(animalDao.deleteById(animal.getId()), "deleteCustomerById(): Asiakkaan poisto ei onnistunut.");
        assertNull(animalDao.findById(animal.getId()), "deleteCustomerById(): Asiakkaan poisto ei onnistunut - asiakan voitiin hakea tietokannasta.");
        // Olemattoman valuutan poiston tulee "epäonnistua"
        assertFalse(animalDao.deleteById(0L), "deleteCustomerById(): Väittää poistaneensa olemattoman asiakkaan.");
    }

    @Test
    @Order(1)
    @DisplayName("Lisätään eläin tietokantaan")
    public void testAddAnimal() {
        assertTrue(animalDao.add(animal));
    }

    @Test
    @DisplayName("Samaa asiakasta ei saa lisätä toistamiseen")
    @Order(2)
    public void testAddAnimal2() {
        assertTrue(animalDao.add(animal), "addAnimal(): Uuden eläimen lisääminen ei onnistu.");
        assertFalse(animalDao.add(animal), "addAnimal(): Saman eläimen voi lisätä kahteen kertaan.");
    }

    @Test
    @Order(3)
    @DisplayName("Etsitään lisätty eläin tietokannasta")
    public void testFindByIdAnimal() {
        animalDao.add(animal);
        // Nyt haun tulee onnistua ja jokaisen kentän tulee tietenkin olla asetettu oikein
        animal = animalDao.findByName(aNimi);
        assertNotNull(animal, "addAnimal(): Juuri lisätyn elukan hakeminen ei onnistunut");
        assertEquals(aNimi, animal.getName(), "getName(): Elukan nimi on väärin.");
        assertEquals(aRotu, animal.getBreed(), "getBreed(): Rotu on väärin.");
    }

    @Test
    @Order(4)
    @DisplayName("Etsitään eläin nimen perusteella")
    public void testFindByNameAnimal() {
        animalDao.add(animal);
        Animal animal2 = animalDao.findByName(animal.getName());
        assertEquals(animal, animal2);
    }

    @Test
    @Order(5)
    @DisplayName("Etsitään kaikki eläimet - tietokannassa on yksi eläin")
    public void testFindAllAnimal() {
        animalDao.add(animal);
        assertTrue(animalDao.findAll().size() > 0);
    }

    @Test
    @Order(6)
    @DisplayName("Etsitään kaikki eläimet - tietokannassa ei ole eläimiä")
    public void testFindAllAnimal2() {
        animalDao.add(animal);
        List<Animal> animals = animalDao.findAll();
        for (Animal a : animals) {
            animalDao.deleteById(a.getId());
        }
        assertEquals(0, animalDao.findAll().size());
    }

    @Test
    @Order(7)
    @DisplayName("Päivitetään eläimen nimi")
    public void testUpdateAnimalName() {
        animalDao.add(animal);
        animal.setName("Maukku");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Maukku", animal.getName(), "getName(): Eläimen nimi päivitys ei onnistunut.");
    }

    @Test
    @Order(8)
    @DisplayName("Päivitetään eläimen tyyppi")
    public void testUpdateAnimalType() {
        animalDao.add(animal);
        animal.setType("Kissa");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Kissa", animal.getType(), "getType(): Eläimen tyypin päivitys ei onnistunut.");
    }

    @Test
    @Order(9)
    @DisplayName("Päivitetään eläimen rotu")
    public void testUpdateAnimalBreed() {
        animalDao.add(animal);
        animal.setBreed("Villikissa");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("Villikissa", animal.getBreed(), "getBreed(): Eläimen rodun päivittäminen ei onnistunut.");
    }

    @Test
    @Order(11)
    @DisplayName("Päivitetään eläimen kuvaus")
    public void testUpdateAnimalDescription() {
        animalDao.add(animal);
        animal.setDescription("mjäyy");
        assertTrue(animalDao.update(animal));
        animal = animalDao.findById(animal.getId());
        assertEquals("mjäyy", animal.getDescription(), "getDescription(): Eläimen kuvaus kentän päivitys ei onnistunut.");
    }

    @Test
    @Order(12)
    @DisplayName("Poistetaan eläin tietokannasta")
    public void testDeleteByIdAnimal() {
        animalDao.add(animal);
        assertTrue(animalDao.deleteById(animal.getId()));
    }

    @Test
    @Order(13)
    @DisplayName("Etsitään poistettu eläin")
    public void testFindByIdAnimal2() {
        animalDao.add(animal);
        animalDao.deleteById(animal.getId());
        assertNull(animalDao.findById(animal.getId()), "deleteByIdAnimal(): Animalin poisto ei onnistunut - eläin voitiin hakea tietokannasta.");
    }

    @Test
    @Order(14)
    @DisplayName("Etsitään poistettu eläin nimen perusteella")
    public void testFindByNameAnimal2() {
        animalDao.add(animal);
        animalDao.deleteById(animal.getId());
        assertNull(animalDao.findByName(animal.getName()));
    }

    @Test
    @DisplayName("Olemattoman eläimen poistoyrityksen tulee palauttaa false")
    @Order(15)
    public void testDeleteByIdAnimal2() {
        assertFalse(animalDao.deleteById(0L));
    }

    @Test
    @DisplayName("Olemattoman eläimen päivitysyrityksen tulee palauttaa false")
    @Order(16)
    public void testUpdateAnimal2() {
        animal.setId(9999L);
        assertFalse(animalDao.update(animal));
    }

    @Test
    @DisplayName("Eläimen pehmeä poistaminen databasesta")
    @Order(17)
    public void testSoftDeleteAnimal() {
        animalDao.add(animal);
        animal.setDeleted();
        assertTrue(animalDao.update(animal), "Päivitetyn eläimen tallennus ei onnistunut");
        assertNull(animalDao.findById(animal.getId()), "Eläimen pehmeä poistaminen ei onnistunut");
        //assertEquals(1, animalDao.findById(animal.getId()).isDeleted(), "Eläimen pehmeä poistaminen ei onnistunut");
    }

}
