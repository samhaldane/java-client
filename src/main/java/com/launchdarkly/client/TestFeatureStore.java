package com.launchdarkly.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A decorated {@link InMemoryFeatureStore} which provides functionality to create (or override) true or false feature flags for all users.
 * <p>
 * Using this store is useful for testing purposes when you want to have runtime support for turning specific features on or off.
 */
public class TestFeatureStore extends InMemoryFeatureStore {
  private static List<JsonElement> TRUE_FALSE_VARIATIONS = Arrays.asList(
      (JsonElement) (new JsonPrimitive(true)),
      (JsonElement) (new JsonPrimitive(false))
  );

  private AtomicInteger version = new AtomicInteger(0);

  /**
   * Sets the value of a boolean feature flag for all users.
   *
   * @param key the key of the feature flag
   * @param value the new value of the feature flag
   */
  public void setBooleanValue(String key, Boolean value) {
    FeatureFlag newFeature = new FeatureFlagBuilder(key)
            .on(false)
            .offVariation(value ? 0 : 1)
            .variations(TRUE_FALSE_VARIATIONS)
            .version(version.incrementAndGet())
            .build();
    upsert(key, newFeature);
  }

  /**
   * Turns a feature, identified by key, to evaluate to true for every user. If the feature rules already exist in the store then it will override it to be true for every {@link LDUser}.
   * If the feature rule is not currently in the store, it will create one that is true for every {@link LDUser}.
   *
   * @param key the key of the feature flag to evaluate to true.
   */
  public void setFeatureTrue(String key) {
    setBooleanValue(key, true);
  }

  /**
   * @deprecated use {@link #setFeatureTrue(String key)}
   */
  @Deprecated
  public void turnFeatureOn(String key) {
    setFeatureTrue(key);
  }

  /**
   * Turns a feature, identified by key, to evaluate to false for every user. If the feature rules already exist in the store then it will override it to be false for every {@link LDUser}.
   * If the feature rule is not currently in the store, it will create one that is false for every {@link LDUser}.
   *
   * @param key the key of the feature flag to evaluate to false.
   */
  public void setFeatureFalse(String key) {
    setBooleanValue(key, false);
  }

  /**
   * @deprecated use {@link #setFeatureFalse(String key)}
   */
  @Deprecated
  public void turnFeatureOff(String key) {
    setFeatureFalse(key);
  }

  /**
   * Sets the value of an integer multivariate feature flag, for all users.
   * @param key the key of the flag
   * @param value the new value of the flag
     */
  public void setIntegerValue(String key, Integer value) {
    setJsonValue(key, new JsonPrimitive(value));
  }

  /**
   * Sets the value of a double multivariate feature flag, for all users.
   * @param key the key of the flag
   * @param value the new value of the flag
     */
  public void setDoubleValue(String key, Double value) {
    setJsonValue(key, new JsonPrimitive(value));
  }

  /**
   * Sets the value of a string multivariate feature flag, for all users.
   * @param key the key of the flag
   * @param value the new value of the flag
     */
  public void setStringValue(String key, String value) {
    setJsonValue(key, new JsonPrimitive(value));
  }

  /**
   * Sets the value of a JsonElement multivariate feature flag, for all users.
   * @param key the key of the flag
   * @param value the new value of the flag
     */
  public void setJsonValue(String key, JsonElement value) {
    FeatureFlag newFeature = new FeatureFlagBuilder(key)
            .on(false)
            .offVariation(0)
            .variations(Arrays.asList(value))
            .version(version.incrementAndGet())
            .build();
    upsert(key, newFeature);
  }
}
