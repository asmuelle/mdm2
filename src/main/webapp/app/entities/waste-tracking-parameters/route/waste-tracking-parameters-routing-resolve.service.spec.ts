import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap, RouterStateSnapshot } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';
import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';

import wasteTrackingParametersResolve from './waste-tracking-parameters-routing-resolve.service';

describe('WasteTrackingParameters routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: WasteTrackingParametersService;
  let resultWasteTrackingParameters: IWasteTrackingParameters | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(WasteTrackingParametersService);
    resultWasteTrackingParameters = undefined;
  });

  describe('resolve', () => {
    it('should return IWasteTrackingParameters returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        wasteTrackingParametersResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWasteTrackingParameters = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWasteTrackingParameters).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        wasteTrackingParametersResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWasteTrackingParameters = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWasteTrackingParameters).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWasteTrackingParameters>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        wasteTrackingParametersResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultWasteTrackingParameters = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWasteTrackingParameters).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
