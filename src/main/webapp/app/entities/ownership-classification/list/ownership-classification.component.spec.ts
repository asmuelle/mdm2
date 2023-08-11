import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnershipClassificationService } from '../service/ownership-classification.service';

import { OwnershipClassificationComponent } from './ownership-classification.component';

describe('OwnershipClassification Management Component', () => {
  let comp: OwnershipClassificationComponent;
  let fixture: ComponentFixture<OwnershipClassificationComponent>;
  let service: OwnershipClassificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'ownership-classification', component: OwnershipClassificationComponent }]),
        HttpClientTestingModule,
        OwnershipClassificationComponent,
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
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(OwnershipClassificationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnershipClassificationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OwnershipClassificationService);

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
    expect(comp.ownershipClassifications?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ownershipClassificationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOwnershipClassificationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOwnershipClassificationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
