/* eslint-disable max-len */
/* eslint-disable require-jsdoc */
import { EventsRepository } from "../events/data/EventsRepository";
import { EventsRepositoryImpl } from "../events/data/EventsRepositoryImpl";
import { EventsService } from "../events/service/EventsService";
import { EventsServiceImpl } from "../events/service/EventsServiceImpl";
import { Reference } from "firebase-admin/database";
import { references } from "../firebase/References";
import { ErrorToServiceErrorResponseMapper } from "../core/mapping/ErrorToServiceErrorResponseMapper";
import { EventModelMapper, EventModelMapperImpl } from "../events/data/mapping/EventModelMapper";
import { CirclesRepository } from "../circles/data/CirclesRepository";
import { circlesServiceLocator } from "./CirclesServiceLocator";

/**
 * Dependencies for the Events module
 */
export class EventsServiceLocator {
  private _circlesReference: Reference | undefined = undefined;
  getCirclesReference(): Reference {
    return this._circlesReference || (this._circlesReference = references.circles);
  }

  private _eventModelMapper: EventModelMapper | undefined;
  getEventModelMapper(): EventModelMapper {
    return this._eventModelMapper || (this._eventModelMapper = new EventModelMapperImpl());
  }

  private _circlesRepository: CirclesRepository | undefined;
  getCirclesRepository(): CirclesRepository {
    return this._circlesRepository || (this._circlesRepository = circlesServiceLocator.getCirclesRepository());
  }

  private _eventsRepository: EventsRepository | undefined;
  getEventsRepository(): EventsRepository {
    return this._eventsRepository || (this._eventsRepository = new EventsRepositoryImpl(
      this.getCirclesReference(),
      this.getCirclesRepository(),
      this.getEventModelMapper(),
    ));
  }


  private _eventsService: EventsService | undefined;
  getEventsService(): EventsService {
    return this._eventsService || (this._eventsService = new EventsServiceImpl(
      this.getEventsRepository()
    ));
  }

  private _errorToServiceErrorResponseMapper: ErrorToServiceErrorResponseMapper | undefined;
  getErrorToServiceErrorResponseMapper(): ErrorToServiceErrorResponseMapper {
    return this._errorToServiceErrorResponseMapper || (this._errorToServiceErrorResponseMapper = new ErrorToServiceErrorResponseMapper());
  }
}

export const eventsServiceLocator = new EventsServiceLocator();

export function getEventsService(): EventsService {
  return eventsServiceLocator.getEventsService();
}
