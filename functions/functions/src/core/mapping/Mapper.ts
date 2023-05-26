/**
 * Base definition for data mappers
 */
export interface Mapper<I, O> {
  map(value: I): O
}
