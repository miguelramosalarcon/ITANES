package pe.miguelramos.itanes.data.local.seed;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelramos.itanes.data.local.entity.PlaceEntity;
import pe.miguelramos.itanes.data.repository.PlaceRepository;

public class PlaceDataSeeder {

    private static final String TAG = "PlaceDataSeeder";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void seed(Application application) {
        executor.execute(() -> {
            PlaceRepository repository = new PlaceRepository(application);
            int count = repository.getCount();

            if (count == 0) {
                Log.d(TAG, "La tabla places está vacía. Iniciando carga de datos...");
                List<PlaceEntity> initialPlaces = getInitialPlaces();
                repository.insertAll(initialPlaces);
                int finalCount = repository.getCount();
                Log.d(TAG, "PlaceDataSeeder: " + finalCount + " lugares disponibles en Room");
            } else {
                Log.d(TAG, "PlaceDataSeeder: Ya existen " + count + " lugares en Room. Omitiendo carga.");
            }
        });
    }

    private static List<PlaceEntity> getInitialPlaces() {
        List<PlaceEntity> places = new ArrayList<>();

        places.add(new PlaceEntity(
                1,
                "Castillo de Chancay",
                "Impresionante castillo de arquitectura medieval a orillas del Océano Pacífico.",
                "El Castillo de Chancay es un complejo turístico y cultural de estilo medieval renacentista, construido sobre acantilados rocosos con vista al mar. Fue edificado por iniciativa de Doña Consuelo Amat y León en la primera mitad del siglo XX. El recinto cuenta con torres, salones, pasadizos subterráneos, piscinas, museos y zonas de entretenimiento, convirtiéndose en uno de los atractivos más visitados de la costa norte chica.",
                "Av. Castillo s/n, Zona Turística, Chancay, Perú",
                -11.5833,
                -77.2667,
                "https://media-cdn.tripadvisor.com/media/attractions-splice-spp-720x480/15/73/60/e2.jpg",
                1,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                2,
                "Complejo Arqueológico de Rupac",
                "Ciudadela preincaica conocida como el \"Machu Picchu costeño\".",
                "Rupac es un importante sitio arqueológico perteneciente a la cultura Atavillos. Ubicado a más de 3,500 metros sobre el nivel del mar, destaca por sus edificaciones de piedra de hasta tres pisos con techos en forma de cúpula y chullpas funerarias en excelente estado de conservación. Es famoso por ofrecer un impresionante fenómeno de \"mar de nubes\" al atardecer y al amanecer, ideal para el trekking y el turismo de aventura.",
                "Distrito de Acos, Provincia de Huaral, Lima, Perú",
                -11.3833,
                -76.8167,
                "https://peru.travel/Contenido/Uploads/in2_637314587031780583.jpg",
                2,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                3,
                "Albufera de Medio Mundo",
                "Hermosa reserva natural de humedales costeros rica en biodiversidad.",
                "La Albufera de Medio Mundo es un área natural protegida que consiste en una gran laguna costera separada del mar por una franja de arena. Es un santuario ecológico que alberga a una gran variedad de aves residentes y migratorias, totorales y flora silvestre. Cuenta con zonas habilitadas para el ecoturismo, paseos en bote, observación de aves y conexión directa con una playa virgen.",
                "Km 177 de la Panamericana Norte, Vegueta, Huacho, Perú",
                -10.9667,
                -77.6167,
                "https://consultasenlinea.mincetur.gob.pe/fichaInventario//foto.aspx?cod=430563",
                3,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                4,
                "Ciudad Sagrada de Caral",
                "La civilización más antigua de América, Patrimonio de la Humanidad.",
                "Caral es el yacimiento arqueológico núcleo de la civilización Caral-Supe, considerada la más antigua de América con una antigüedad aproximada de 5,000 años, contemporánea a las pirámides de Egipto. El complejo destaca por su compleja planificación urbana, grandes pirámides truncadas, plazas circulares hundidas y un sistema social avanzado desarrollado en el valle de Supe.",
                "Valle de Supe, Provincia de Barranca, Lima, Perú",
                -10.8931,
                -77.5219,
                "https://media-cdn.tripadvisor.com/media/attractions-splice-spp-720x480/0b/04/c0/c1.jpg",
                4,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                5,
                "Fortaleza de Paramonga",
                "Monumental pirámide de adobe de la cultura Chimú e Inca.",
                "La Fortaleza de Paramonga es un complejo arqueológico constituido principalmente por grandes estructuras de adobe distribuidas en forma de terrazas escalonadas que simulan la figura de un animal mítico (un puma o un dragón). Construida originalmente por la cultura Chimú y posteriormente adaptada por los incas como un centro administrativo y militar estratégico en la ruta del Qhapaq Ñan.",
                "Distrito de Paramonga, Provincia de Barranca, Lima, Perú",
                -10.6833,
                -77.8167,
                "https://consultasenlinea.mincetur.gob.pe/fichaInventario//foto.aspx?cod=337856",
                5,
                "2026-08-25"
        ));

        return places;
    }
}
