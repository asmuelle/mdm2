import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';

import { WasteTrackingParametersComponent } from './waste-tracking-parameters.component';

describe('WasteTrackingParameters Management Component', () => {
  let comp: WasteTrackingParametersComponent;
  let fixture: ComponentFixture<WasteTrackingParametersComponent>;
  let service: WasteTrackingParametersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'waste-tracking-parameters', component: WasteTrackingParametersComponent }]),
        HttpClientTestingModule,
        WasteTrackingParametersComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(WasteTrackingParametersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WasteTrackingParametersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WasteTrackingParametersService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.wasteTrackingParameters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to wasteTrackingParametersService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getWasteTrackingParametersIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getWasteTrackingParametersIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
