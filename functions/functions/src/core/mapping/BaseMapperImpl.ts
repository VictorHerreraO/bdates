import { Mapper } from "./Mapper";

/**
 * Base Mapper Implementation
 */
export abstract class BaseMapperImpl<I, O> implements Mapper<I, O> {
  abstract map(value: I): O

  /**
   * Casts `value` as any and removes `id` property.
   * @param {O} value value to reverse map
   * @return {any} reverse mapped value
   */
  reverseMap(value: O): any {
    const obj = value as any;
    delete obj.id;
    return obj;
  }

  /**
   * Iterates using `Values.forEach` and maps each item using `map`
   * @param {Values<T>} values values
   * @return {Array<O>} mapped values
   */
  mapItems(values: Values<I>): Array<O> {
    const items: Array<O> = [];
    values.forEach((item: I) => {
      items.push(this.map(item));
    });
    return items;
  }
}

export interface Values<T> {
  forEach(callback: (item: T) => void): any;
}
