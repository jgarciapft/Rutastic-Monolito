package helper.model;

import model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory of {@code ModelMapper} implemented using a Singleton instance. To get the Singleton instance call the
 * method {@link #get()}.
 *
 * @see ModelMapper
 */
public class ModelMapperFactory {

    private final Map<Class<?>, ModelMapper<?>> modelMapperCollection;

    private ModelMapperFactory() {
        HashMap<Class<?>, ModelMapper<?>> modifiableFactory = new HashMap<>();

        // Instantiate and store an instance of each model mapper to be reused

        modifiableFactory.put(User.class, new UserModelMapper());
        modifiableFactory.put(Route.class, new RouteModelMapper());
        modifiableFactory.put(RouteCategory.class, new RouteCategoryModelMapper());
        modifiableFactory.put(RouteToCategoriesMapping.class, new RouteToCategoryMappingMapper());
        modifiableFactory.put(KudoEntry.class, new KudoEntryMapper());

        modelMapperCollection = Collections.unmodifiableMap(modifiableFactory);
    }

    /**
     * @return The Singleton model mapper factory instance
     */
    public static ModelMapperFactory get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @param modelClass The class of the model for which a model mapper is requested
     * @param <T>        The model for which a model mapper is requested
     * @return A {@code ModelMapper} for the requested model class or null if it couldn't be found
     */
    @SuppressWarnings("unchecked")
    public <T> ModelMapper<T> forModel(Class<T> modelClass) {
        ModelMapper<?> modelMapper = modelMapperCollection.getOrDefault(modelClass, null);

        // Return the model mapper for the requested model class or null if it couldn't be found

        if (modelMapper != null)
            return (ModelMapper<T>) modelMapper;
        else
            return null;
    }

    /**
     * Singleton holder for ModelMapperFactory class
     */
    private static class SingletonHolder {
        private static final ModelMapperFactory INSTANCE = new ModelMapperFactory();
    }
}
