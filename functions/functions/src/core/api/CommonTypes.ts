export type UserId = string

export type ModelUpdate<T> = {
  [key in keyof T]: unknown
}
