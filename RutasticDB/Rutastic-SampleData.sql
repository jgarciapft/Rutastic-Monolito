PRAGMA foreign_keys = ON;

-- Users: 1 admin, 6 regular users

INSERT INTO users(username, email, password, role)
VALUES ('pablogpz', 'jnpblgpz@gmail.com', 'uex', 'admin');
INSERT INTO users(username, email, password, role)
VALUES ('lorenGar', 'lorenagarcia@gmail.com', 1234, 'user');
INSERT INTO users(username, email, password)
VALUES ('raulPerez', 'raulperez@gmail.com', 1234);
INSERT INTO users(username, email, password)
VALUES ('david20', 'hondo@gmail.com', 1234);
INSERT INTO users(username, email, password)
VALUES ('juliaaan', 'juli@gmail.com', 1234);
INSERT INTO users(username, email, password)
VALUES ('jgarciapft', 'jgarciapft@alumnos.unex.es', 1234);
INSERT INTO users(username, email, password)
VALUES ('robert123', 'robertoytal@hotmail.es', 1234);

-- Routes

INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level, blocked)
VALUES (1, 'Villanueva Mesia', 'Ruta más que conocida para todos los ciclistas granadinos, ya que es muy frecuentada por su buen asfalto y poco tráfico, excepto de Granada a Pinos Puente, pero a la vuelta tiramos por Fuente Vaqueros y es un carretera preciosa.

Durante toda la marcha se puede andar bien ya que no hay ninguna subida de relevancia.

En 2013 habitaban Granada 237,818 personas, 498.365 contando el área metropolitana. Los barrios que posee son muy diferentes entre sí, en parte por la continuada inmigración acaecida hasta la década de 1990; los más importantes son el Zaidín, el Albaicín, el Sacromonte, el Realejo, La Chana, Almanjáyar y la Cartuja. Granada fue capital del Reino Zirí de Granada, durante el siglo XI, y del Reino Nazarí de Granada entre los siglos XIII y XV. Tras la toma de la ciudad por los Reyes Católicos, se mantuvo como capital del Reino castellano de Granada, que ya era una simple jurisdicción territorial y que se mantuvo hasta 1833, momento en que se produjo una nueva división provincial en España, todavía vigente. Su escudo municipal ostenta los títulos de «Muy noble, muy leal, nombrada, grande, celebérrima y heroica ciudad de Granada». Granada constituye un núcleo receptor de turismo, debido a sus monumentos y a la cercanía de su estación de esquí profesional, así como a la zona histórica conocida como La Alpujarra y también a la parte de la costa granadina conocida como Costa Tropical. De entre sus construcciones históricas, la Alhambra es una de las más importantes del país, declarada Patrimonio de la Humanidad por la Unesco en 1984, junto con el jardín del Generalife y el Albaicín. Su catedral está considerada como la primera iglesia renacentista de España. La Universidad de Granada es la cuarta a nivel nacional por número de alumnos y es uno de los destinos más populares por los universitarios europeos del programa Erasmus. En 2011, recibió del Ministerio de Educación un anticipo de 1,8 millones de euros en concepto de remuneración del personal investigador en formación; becas, ayudas y contratos de 256 investigadores dentro del programa de formación del profesorado.',
        10146, 156, 666, 'facil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level,
                    blocked)
VALUES (2, 'Ruta del Cares', 'La Ruta del Cares se abrió en la roca para el mantenimiento del canal de alimentación de la central hidroeléctrica de Camarmeña - Poncebos.

La Ruta del Cares está situada en el Parque Nacional de los Picos de Europa. Transcurre entre las localidades de Caín y Poncebos, atravesando el desfiladero que sigue el río en una de las rutas de senderismo más espectaculares que se pueden hacer en toda Europa.

Conocida como la “Garganta Divina”, la ruta , tallada literalmente en las rocas de las montañas, es un trayecto maravilloso de un poco más de 11 kilómetros de distancia entre el pueblo de Caín (León) y Poncebos (Asturias).',
        13000, 233, 593, 'media', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level, blocked)
VALUES (1, 'Camino Santiago Francés - Roncesvalles - Santiago de Compostela', 'Ante todo cada persona hace su camino por motivos variados (Personales, deporte, espirituales, religiosos, “conocer personas,” etc.). Y cada camino es un mundo.
Muchos dirán que el camino francés puro empieza en Saint Jean Pied de Port y no les quito la razón pero yo desde pequeño siempre escuchaba Roncesvalles como comienzo del camino.

Explicare someramente mi ruta y lo que yo he vivido, solamente tenemos que poner en google y nos salen 25.600.000 resultados; yo de todo eso he utilizado dos páginas que me parecen interesantes pulsa link Eroski y Gronze para mi muy completas, incluso en la de Eroski te puedes descargar los PDF para meter en el teléfono y van fenomenal.

Durante las 30 rutas que he realizado prácticamente en todas he salido a las 06:00 de la madrugada por lo tanto el GPS y el frontal me han venido de perilla y estar muy atentos en las bifurcaciones para ver las flechas y las conchas. En este primer recorrido del Camino de Santiago - Francés recorreremos los bellos paisajes y poblaciones que une Roncesvalles y Burgos, poblaciones como: Zubiri con su famoso puente románico, Pamplona con su catedral y su casco histórico, Puente la Reina con su puente y su casco antiguo,Estella y su puente,Irache y su fuente de vino,Los Arcos la iglesia de Santa María

Logroño su catedral y tapear por la calle Laurel, Najera su puente y casco histórico

Santo Domingo su catedral,Belorado su iglesia de Santa María,San Juan de Ortega y su monasterio y Burgos su catedral y su casco histórico la ancha Meseta,que se hace un poco cansina ,el Canal de Castilla con sus esclusas,en Bercianos con sus casas de adobe y así llegamos a León con su majestuosa Catedral.
A partir de León todo va cambiando progresivamente para entrar en montaña poco apoco pasito a pasito llegamos a la Cruz de Hierro y su impresionante bajada hacia Ponferrada pronto llegamos a O´Cebreiro y entramos en la verde Galicia pequeñas subidas y bajadas durante todo este tierra que nos llevara a la Plaza del Obradoiro y poder abrazar a Santiago.',
        105240, 10400, 1724, 'dificil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
                    blocked)
VALUES (2, 'Desfiladero de Las Xanas', 'Hoy vamos a recorrer una de las rutas más emblemática de Asturias y con gran parecido, salvando las distancias, a la Senda del Cares por su semejanza en su construcción en muchos de sus tramos.

Este desfiladero de la Cordillera Cantábrica del norte asturiano, llega a alcanzar una profundidad de hasta 80-90 metros, y tiene una longitud de alrededor de dos kilómetros de garganta tallados en la cara Oeste de los montes de la Sierra del Aramo con increíble audacia por el arroyo Viescas o de las Xanas que se dirige en su descenso encabritado en una sucesión de estratos calizos a desembocar hacia al río Trubia. Gran parte de su paseo peatonal discurre por la margen derecha del río, excavado en la roca vertical a media ladera en la montaña, bordeando espectaculares cortados y vertiginosos escenarios, se recorre de oeste a este, mediante un sendero encañonado lleno de belleza y encanto natural, dominado por un perfil rocoso en cuyo lecho yermo crecen madroños, encinas y tejos, que avanza por una sinuosa y estrecha vía que atraviesa varios puentes de madera y túneles tallados en la roca caliza, no superando en la mayoría de su trayecto los dos metros de ancho. Esta pista pedregosa pertenecía a un antiguo propósito de construir una carretera que sacara del aislamiento a los pueblos de Pedroveya, Rebollada y Dosango y los comunicara con el valle principal, el que recorre el Río Trubia a menos de 25 kilómetros de Oviedo.',
        9000, 486, 564, 1572998400, 'media', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
                    blocked)
VALUES (2, 'Vereda de la Estrella Corta', 'Conocidísima ruta, por las faldas del Mulhacen y La Alcazaba',
        5398, 72, 1652, 1594913520, 'facil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level,
                    blocked)
VALUES (3, 'Ruta Yaiza-Playa Blanca',
        'Ruta senderista partiendo del pueblo de Yaiza y con llegada al puerto Marina Rubicón de Playa Blaca isla de Lanzarote',
        26920, 870, 202, 'dificil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level,
                    blocked)
VALUES (3, 'Vuelta a Lanzarote y La Graciosa', 'Primera parte del día visitando el pueblo costero de El Golfo y el Charco de los Clicos. Conoceremos también los miradores de los Hervideros y la laguna de Janubio.

Llegamos a Playa Blanca, localidad turística, y paseamos frente a la costa. A continuación, lo más duro del día; volvemos a los caminos solitarios y pedregosos para atravesar el Monumento Natural de Los Ajaches. En su primera mitad, encontraremos un sendero pedregoso llevadero. En la segunda mitad, tocará empujar la bici en varias ocasiones debido a la elevada pendiente (ojo en las bajadas). Preciosas vistas del océano y de las localidades cercanas.

Cruzamos Playa Quemada y Puerto Calero, para poner fin a nuestra aventura en Puerto del Carmen',
        14870, 101, 163, 'facil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
                    blocked)
VALUES (4, 'Corral del Diablo (3 lagunas) + Pico de la Nava', 'Excelente ruta de alta montaña de gran belleza por el Macizo Occidental de Gredos, la ruta es exigente físicamente, ya que salva gran desnivel tanto en la subida como en la bajada y corona cresteando 5 cotas superiores a los 2300 metros con unas vistas excelentes de la provincia de Cáceres y Ávila.

Recorrido descriptivo:
Iniciamos la ruta en el viernes a las 19:15 en el aparcamiento habilitado para la ruta entre la población de Umbrías y Nava del Barco. Con algo de prisa seguimos las marcas blancas y amarillas del sendero PR AV 39 que nos conduce hasta la Laguna de la Nava, conocida también como Corral del Diablo. En 1 hora primero por camino y luego por senda llegamos hasta el primer refugio, el de Navacasera. Allí haremos noche, unos filetes a golpe de hornillo, una tortilla de patatas y al saco mientras vemos un episodio de Desafío Extremo en el Iphone',
        32360, 2422, 1440, 1594572000, 'dificil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
                    blocked)
VALUES (5, 'Cavalls', 'El recorrido de 24 horas requiere una preparación específica para largas distancias. Podemos encontrarnos con condiciones meteorológicas complicadas y hay que saber orientarse por métodos tradicionales con el mapa que te entregan y/o manejar medios electrónicos de orientación. La señalización es insuficente en algunos tramos, sobre todo para recorridos nocturnos: sería deseable una renovación de la señalización (en algunos tramos muy difusa) y una indicación de distancias entre refugios ( similar a la existente en Suiza, Francia, Italia, etc). Los postes indicativos del parque natural sólo refieren distancias a puntos entre pistas. Ojo con el recorrido entre Cortals d´Ingla y Prats d´Aguiló si hay que realizarlo de noche y con niebla: el recorrido antiguo es un laberinto de árboles caídos en un bosque de pino negro y el recorrido nuevo puede confundir. Sería interesante la posibilidad de encontrar refugio en cualquiera de los refugios del recorrido en caso de emergencia cuando sea necesario resguardarse ( tormentas , vientos fuertes, niebla de noche, lesiones, accidentes leves )aunque suponga aumentar la tarifa del coste del recorrido, habilitando algún lugar en el dormitorio o una zona libre de tránsito y pernocta.
Por lo demás el recorrido es de gran belleza y permite la observación de todos los pisos bioclimáticos del Pirineo con interesantes intromisiones del ámbito mediterráneo, así como diversidad de taxones florísticos o de fauna habitual en el parque.
En cuanto al recorrido, habitualmente la gente lo realiza en sentido antihorario: creo que es más duro por cuanto hay que salvar casi de contínuo un desnivel de 2.800 m. entre el refugio de Gressolet y el Niu d´Aliga ( dos bajadas relativamente cortas ) y más de 1.800 m de desnivel positivo contínuo desde la carretera.',
        26000, 500, 2326, 1594890480, 'media', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, creation_date, skill_level,
                    blocked)
VALUES (5, 'Xàtiva - Cova Negra (Alboi)',
        'Ruta que comienza muy cerca de Xàtiva y que nos conduce, por parajes encantadores, hasta la "Casa de la Llum" pasando por "les arcaetes" y la "Cova Negra" de Alboi. Paseo muy recomendable para una tarde de primavera.',
        5480, 72, 137, 1594793400, 'facil', 0);
INSERT INTO routes (created_by_user, title, description, distance, duration, elevation, skill_level,
                    blocked)
VALUES (6, 'Fuerteventura norte',
        'Ruta por el norte de Fuerteventura visitando lugares tan interesantes como La Oliva, Ecomuseo de La Alcogida, Villaverde, la Cueva del Llano, Tindaya, El Cotillo y sus playas...',
        900, 32, 322, 'facil', 0);

-- Route categories

INSERT INTO routetocategoriesmapping (route, category)
VALUES (1, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (1, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (2, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (3, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (3, 3);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (4, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (4, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (4, 3);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (5, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (5, 3);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (6, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (7, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (7, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (8, 3);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (9, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (9, 2);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (9, 3);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (10, 1);
INSERT INTO routetocategoriesmapping (route, category)
VALUES (11, 2);

-- Users give kudos to routes

INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (1, 1, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 1, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (3, 1, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (4, 1, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (5, 1, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (6, 1, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (7, 1, 1);

INSERT INTO routekudosregistry (user, route, modifier)
VALUES (1, 4, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 4, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (4, 4, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (6, 4, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (7, 4, -1);

INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (1, 2, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (1, 5, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (1, 9, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (1, 11, -1, 1594425600);

INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 3, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 10, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 7, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (2, 5, -1);

INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (3, 6, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (3, 9, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (3, 10, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (3, 11, -1);

INSERT INTO routekudosregistry (user, route, modifier)
VALUES (4, 8, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (4, 7, -1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (4, 6, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (4, 10, -1, 1594425600);

INSERT INTO routekudosregistry (user, route, modifier)
VALUES (5, 5, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (5, 6, -1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (5, 7, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (5, 8, -1);

INSERT INTO routekudosregistry (user, route, modifier)
VALUES (6, 3, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (6, 7, -1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (6, 8, 1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (6, 11, -1);

INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (7, 2, 1, 1594425600);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (7, 5, -1);
INSERT INTO routekudosregistry (user, route, modifier)
VALUES (7, 6, 1);
INSERT INTO routekudosregistry (user, route, modifier, submission_date)
VALUES (7, 11, -1, 1594425600);