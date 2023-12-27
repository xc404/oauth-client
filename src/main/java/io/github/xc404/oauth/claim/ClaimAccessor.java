/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xc404.oauth.claim;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


/**
 * An &quot;accessor&quot; for a set of claims that may be used for assertions.
 *
 * @author Joe Grandja
 * @since 5.0
 */
public interface ClaimAccessor
{

    static void notNull(Object object, Supplier<String> messageSupplier) {
        if( object == null ) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    /**
     * Returns a set of claims that may be used for assertions.
     *
     * @return a {@code Map} of claims
     */
    Map<String, Object> getClaims();

    /**
     * Returns the claim value as a {@code T} type. The claim value is expected to be of
     * type {@code T}.
     *
     * @param claim the name of the claim
     * @param <T>   the type of the claim value
     * @return the claim value
     * @since 5.2
     */
    @SuppressWarnings("unchecked")
    default <T> T getClaim(String claim) {
        return !hasClaim(claim) ? null : (T) getClaims().get(claim);
    }

    /**
     * Returns {@code true} if the claim exists in {@link #getClaims()}, otherwise
     * {@code false}.
     *
     * @param claim the name of the claim
     * @return {@code true} if the claim exists, otherwise {@code false}
     * @since 5.5
     */
    default boolean hasClaim(String claim) {
        notNull(claim, () -> "claim cannot be null");
        return getClaims().containsKey(claim);
    }

    /**
     * Returns the claim value as a {@code String} or {@code null} if it does not exist or
     * is equal to {@code null}.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if it does not exist or is equal to
     * {@code null}
     */
    default String getClaimAsString(String claim) {
        return !hasClaim(claim) ? null
                : ClaimUtils.toString(getClaim(claim));
    }

    /**
     * Returns the claim value as a {@code Boolean} or {@code null} if the claim does not
     * exist.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if the claim does not exist
     * @throws IllegalArgumentException if the claim value cannot be converted to a
     *                                  {@code Boolean}
     * @throws NullPointerException     if the claim value is {@code null}
     */
    default Boolean getClaimAsBoolean(String claim) {
        if( !hasClaim(claim) ) {
            return null;
        }
        Object claimValue = getClaims().get(claim);
        Boolean convertedValue = ClaimUtils.toBoolean(claim);
        notNull(convertedValue,
                () -> "Unable to convert claim '" + claim + "' of type '" + claimValue.getClass() + "' to Boolean.");
        return convertedValue;
    }

    /**
     * Returns the claim value as an {@code Instant} or {@code null} if it does not exist.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if it does not exist
     */
    default Instant getClaimAsInstant(String claim) {
        if( !hasClaim(claim) ) {
            return null;
        }
        Object claimValue = getClaims().get(claim);
        Instant convertedValue = ClaimUtils.toInstant(claim);
        notNull(convertedValue,
                () -> "Unable to convert claim '" + claim + "' of type '" + claimValue.getClass() + "' to Instant.");
        return convertedValue;
    }

    /**
     * Returns the claim value as an {@code URL} or {@code null} if it does not exist.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if it does not exist
     */
    default URL getClaimAsURL(String claim) {
        if( !hasClaim(claim) ) {
            return null;
        }
        Object claimValue = getClaims().get(claim);
        URL convertedValue = ClaimUtils.toURL(claim);
        notNull(convertedValue,
                () -> "Unable to convert claim '" + claim + "' of type '" + claimValue.getClass() + "' to URL.");
        return convertedValue;
    }

    /**
     * Returns the claim value as a {@code Map<String, Object>} or {@code null} if the
     * claim does not exist.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if the claim does not exist
     * @throws IllegalArgumentException if the claim value cannot be converted to a
     *                                  {@code List}
     * @throws NullPointerException     if the claim value is {@code null}
     */
    default Map<String, Object> getClaimAsMap(String claim) {
        if( !hasClaim(claim) ) {
            return null;
        }
        Object claimValue = getClaims().get(claim);
        Map<String, Object> convertedValue = ClaimUtils.toMap(claim);
        notNull(convertedValue,
                () -> "Unable to convert claim '" + claim + "' of type '" + claimValue.getClass() + "' to Map.");
        return convertedValue;
    }

    /**
     * Returns the claim value as a {@code List<String>} or {@code null} if the claim does
     * not exist.
     *
     * @param claim the name of the claim
     * @return the claim value or {@code null} if the claim does not exist
     * @throws IllegalArgumentException if the claim value cannot be converted to a
     *                                  {@code List}
     * @throws NullPointerException     if the claim value is {@code null}
     */
    default List<String> getClaimAsStringList(String claim) {
        if( !hasClaim(claim) ) {
            return null;
        }
        Object claimValue = getClaims().get(claim);
        List<String> convertedValue = ClaimUtils.toStringList(claim);
        notNull(convertedValue,
                () -> "Unable to convert claim '" + claim + "' of type '" + claimValue.getClass() + "' to List.");
        return convertedValue;
    }

}
